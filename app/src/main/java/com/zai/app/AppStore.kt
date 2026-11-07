package com.zai.app

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppStore {
    var lang by mutableStateOf("system")
        private set
    var theme by mutableStateOf("dark")
        private set
    var desktop by mutableStateOf(false)
        private set

    fun init(ctx: Context) {
        lang = AppPrefs.lang(ctx)
        theme = AppPrefs.theme(ctx)
        desktop = AppPrefs.desktop(ctx)
    }

    fun setLang(ctx: Context, value: String) {
        AppPrefs.setLang(ctx, value)
        lang = value
    }

    fun setTheme(ctx: Context, value: String) {
        AppPrefs.setTheme(ctx, value)
        theme = value
    }

    fun setDesktop(ctx: Context, value: Boolean) {
        AppPrefs.setDesktop(ctx, value)
        desktop = value
    }
}
