package com.zai.app

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object AppPrefs {
    private const val FILE = "zai_settings"
    private const val KEY_LANG = "language"   // system | fa | en
    private const val KEY_THEME = "theme"     // dark | light | system
    private const val KEY_DESKTOP = "desktop" // bool

    private fun prefs(ctx: Context) =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun lang(ctx: Context): String = prefs(ctx).getString(KEY_LANG, "system") ?: "system"
    fun theme(ctx: Context): String = prefs(ctx).getString(KEY_THEME, "dark") ?: "dark"
    fun desktop(ctx: Context): Boolean = prefs(ctx).getBoolean(KEY_DESKTOP, false)

    fun setLang(ctx: Context, value: String) = prefs(ctx).edit().putString(KEY_LANG, value).apply()
    fun setTheme(ctx: Context, value: String) = prefs(ctx).edit().putString(KEY_THEME, value).apply()
    fun setDesktop(ctx: Context, value: Boolean) = prefs(ctx).edit().putBoolean(KEY_DESKTOP, value).apply()

    fun wrapLocale(base: Context): Context {
        val tag = lang(base)
        if (tag == "system") return base
        val locale = Locale.forLanguageTag(tag)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}
