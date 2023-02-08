package kz.busjol.ext

import android.annotation.SuppressLint
import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.reformatDateFromBackend(): String? {
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputFormat = "dd.MM.yyyy"

    return formatDate(this, inputFormat, outputFormat)
}

fun String.reformatDateFromBackendOnlyTime(): String? {
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputFormat = "HH:mm"

    return formatDate(this, inputFormat, outputFormat)
}

fun String.reformatDateToBackend(isDotFormat: Boolean): String? {
    val inputFormat = if (isDotFormat) "dd.MM.yyyy" else "yyyy-MM-dd"
    val outputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    return formatDate(this, inputFormat, outputFormat)
}

@SuppressLint("SimpleDateFormat")
private fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {
    try {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(inputFormat)
                .parse(dateToFormat)?.let {
                    SimpleDateFormat(outputFormat)
                        .format(it)
                }
        } else {
            String()
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

fun String.decodeToken(): String {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
    val parts = this.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        "$header"
        "$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}
