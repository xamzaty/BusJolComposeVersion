package kz.busjol.ext

import android.content.Context
import kz.busjol.utils.NumberFormatter

fun Int.toPx(context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return (this * (metrics.densityDpi.toFloat() / 160.0f)).toInt()
}

fun Int.toDp(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}

fun Number.format(): String = NumberFormatter.format(this)

fun Number.formatWithCurrency(): String = NumberFormatter.formatWithCurrency(this)

fun Number?.containedIn(vararg list: Number): Boolean {
    return if (this == null) false else list.contains(this)
}