package com.zai.app.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zai.app.AppStore
import com.zai.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onClose: () -> Unit,
    onClearBrowsing: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    BackHandler(onBack = onClose)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            SectionTitle(stringResource(R.string.language))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = AppStore.lang == "system",
                    onClick = {
                        AppStore.setLang(context, "system")
                        activity?.recreate()
                    },
                    label = { Text(stringResource(R.string.lang_system)) }
                )
                FilterChip(
                    selected = AppStore.lang == "fa",
                    onClick = {
                        AppStore.setLang(context, "fa")
                        activity?.recreate()
                    },
                    label = { Text(stringResource(R.string.lang_fa)) }
                )
                FilterChip(
                    selected = AppStore.lang == "en",
                    onClick = {
                        AppStore.setLang(context, "en")
                        activity?.recreate()
                    },
                    label = { Text(stringResource(R.string.lang_en)) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(stringResource(R.string.theme))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = AppStore.theme == "system",
                    onClick = { AppStore.setTheme(context, "system") },
                    label = { Text(stringResource(R.string.theme_system)) }
                )
                FilterChip(
                    selected = AppStore.theme == "dark",
                    onClick = { AppStore.setTheme(context, "dark") },
                    label = { Text(stringResource(R.string.theme_dark)) }
                )
                FilterChip(
                    selected = AppStore.theme == "light",
                    onClick = { AppStore.setTheme(context, "light") },
                    label = { Text(stringResource(R.string.theme_light)) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(stringResource(R.string.behavior))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.desktop_mode),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = AppStore.desktop,
                    onCheckedChange = { AppStore.setDesktop(context, it) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onClearBrowsing,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.clear_data))
            }
            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(stringResource(R.string.login_help_title))
            Text(
                text = stringResource(R.string.login_help_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(stringResource(R.string.about_title))
            Text(
                text = stringResource(R.string.about_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.version_label) + " 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.z.ai/")))
                    } catch (_: Exception) {
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.open_in_browser))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}
