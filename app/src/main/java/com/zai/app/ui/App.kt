package com.zai.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zai.app.AppStore
import com.zai.app.ui.theme.ZaiTheme

@Composable
fun ZaiApp() {
    ZaiTheme(themeMode = AppStore.theme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WebViewScreen()
        }
    }
}
