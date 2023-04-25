package kz.busjol.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*

fun setLocale(context: Context, language: String?): Context {
    return updateResources(context, language ?: "kk")
}

private fun updateResources(context: Context, language: String): Context {
    val locale = Locale(language)
    val config = Configuration(context.resources.configuration)

    Locale.setDefault(locale)
    config.setLocale(locale)

    return context.createConfigurationContext(config)
}
