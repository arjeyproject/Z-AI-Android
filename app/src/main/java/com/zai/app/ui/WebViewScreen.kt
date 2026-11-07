package com.zai.app.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.view.View
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.zai.app.AppStore
import com.zai.app.R

private const val BASE_URL = "https://chat.z.ai/"

private fun userAgent(desktop: Boolean): String = if (desktop) {
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
} else {
    "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36"
}

private fun openExternal(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

class WebViewHolder {
    var webView: WebView? = null
}

@Composable
fun WebViewScreen() {
    val holder = remember { WebViewHolder() }
    var showSettings by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        WebViewArea(holder = holder, onOpenSettings = { showSettings = true })

        if (showSettings) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SettingsScreen(
                    onClose = { showSettings = false },
                    onClearBrowsing = {
                        holder.webView?.let { wv ->
                            CookieManager.getInstance().removeAllCookies(null)
                            CookieManager.getInstance().flush()
                            wv.clearCache(true)
                            wv.clearFormData()
                            wv.clearHistory()
                            WebStorage.getInstance().deleteAllData()
                            wv.loadUrl(BASE_URL)
                        }
                        showSettings = false
                    }
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebViewArea(
    holder: WebViewHolder,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current

    var progress by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var pendingFileCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    var pendingPermission by remember { mutableStateOf<PermissionRequest?>(null) }
    var customView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    val fileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val callback = pendingFileCallback
        pendingFileCallback = null
        val uris = WebChromeClient.FileChooserParams.parseResult(result.resultCode, result.data)
        callback?.onReceiveValue(uris)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val request = pendingPermission
        pendingPermission = null
        if (request != null) {
            if (grants.isNotEmpty() && grants.all { it.value }) {
                request.grant(request.resources)
            } else {
                request.deny()
            }
        }
    }

    val webview = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(false)
            setBackgroundColor(0xFF0B0B0D.toInt())
            isFocusableInTouchMode = true
            holder.webView = this
        }
    }

    CookieManager.getInstance().apply {
        setAcceptCookie(true)
        setAcceptThirdPartyCookies(webview, true)
    }

    webview.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url
            val scheme = uri.scheme ?: return false
            if (scheme == "http" || scheme == "https") {
                val host = uri.host ?: return false
                val internal = host == "chat.z.ai" || host.endsWith(".z.ai")
                return if (internal) {
                    false
                } else {
                    openExternal(context, uri)
                    true
                }
            }
            openExternal(context, uri)
            return true
        }

        override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
            canGoBack = view.canGoBack()
        }

        override fun onPageFinished(view: WebView, url: String?) {
            CookieManager.getInstance().flush()
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            if (request.isForMainFrame) {
                errorMsg = error.description?.toString()
            }
        }
    }

    webview.webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progress = newProgress
            loading = newProgress < 100
        }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: FileChooserParams
        ): Boolean {
            pendingFileCallback?.onReceiveValue(null)
            pendingFileCallback = filePathCallback
            return try {
                fileLauncher.launch(fileChooserParams.createIntent())
                true
            } catch (_: Exception) {
                pendingFileCallback = null
                filePathCallback.onReceiveValue(null)
                true
            }
        }

        override fun onPermissionRequest(request: PermissionRequest) {
            val wanted = mutableListOf<String>()
            for (res in request.resources) {
                when (res) {
                    PermissionRequest.RESOURCE_AUDIO_CAPTURE -> wanted.add(Manifest.permission.RECORD_AUDIO)
                    PermissionRequest.RESOURCE_VIDEO_CAPTURE -> wanted.add(Manifest.permission.CAMERA)
                }
            }
            if (wanted.isEmpty()) {
                request.deny()
                return
            }
            val toAsk = wanted.filter {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }
            if (toAsk.isEmpty()) {
                request.grant(request.resources)
            } else {
                pendingPermission = request
                permissionLauncher.launch(toAsk.toTypedArray())
            }
        }

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            customViewCallback?.onCustomViewHidden()
            customView = view
            customViewCallback = callback
        }

        override fun onHideCustomView() {
            customView = null
            customViewCallback = null
        }
    }

    webview.setDownloadListener { url, userAgent, contentDisposition, mimeType, _ ->
        try {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                val name = URLUtil.guessFileName(url, contentDisposition, mimeType)
                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)
                    mimeType?.let { setMimeType(it) }
                    addRequestHeader("User-Agent", userAgent)
                }
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(context, context.getString(R.string.download_started), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context.getString(R.string.download_unsupported), Toast.LENGTH_SHORT).show()
            }
        } catch (_: Exception) {
            Toast.makeText(context, context.getString(R.string.download_unsupported), Toast.LENGTH_SHORT).show()
        }
    }

    var firstRun = remember { true }
    LaunchedEffect(AppStore.desktop) {
        webview.settings.userAgentString = userAgent(AppStore.desktop)
        if (firstRun) {
            firstRun = false
            if (webview.url == null) webview.loadUrl(BASE_URL)
        } else {
            webview.reload()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webview.stopLoading()
            webview.destroy()
            holder.webView = null
        }
    }

    BackHandler(enabled = customView == null && canGoBack) {
        webview.goBack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Z.AI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { errorMsg = null; webview.loadUrl(BASE_URL) }) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.new_chat))
                }
                IconButton(onClick = { errorMsg = null; webview.reload() }) {
                    Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.refresh))
                }
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.settings))
                }
            }

            if (loading && errorMsg == null) {
                LinearProgressIndicator(
                    progress = { progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    trackColor = MaterialTheme.colorScheme.background
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    factory = { webview },
                    modifier = Modifier.fillMaxSize()
                )
                if (errorMsg != null) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ErrorPane(
                            message = errorMsg ?: "",
                            onRetry = {
                                errorMsg = null
                                if (webview.url == null) webview.loadUrl(BASE_URL) else webview.reload()
                            }
                        )
                    }
                }
            }
        }

        customView?.let { cv ->
            BackHandler {
                customViewCallback?.onCustomViewHidden()
                customView = null
                customViewCallback = null
            }
            key(cv) {
                AndroidView(
                    factory = { cv },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ErrorPane(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.error_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = onRetry) {
            Text(stringResource(R.string.error_retry))
        }
    }
}
