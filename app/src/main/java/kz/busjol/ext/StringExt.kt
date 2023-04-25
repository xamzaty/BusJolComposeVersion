package kz.busjol.ext

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.util.Base64

fun String.reformatDateMonthWithWords(): String? {
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputFormat = "dd MMMM"

    return formatDate(this, inputFormat, outputFormat)
}
fun String.reformatFullDateFromBackend(): String? {
    val inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val outputFormat = "dd.MM.yyyy HH:mm"

    return formatDate(this, inputFormat, outputFormat)
}

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

fun String.decodeToken(): String {
    val parts = this.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.decode(parts[0], Base64.URL_SAFE or Base64.NO_WRAP), charset)
        val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP), charset)
        "$header\n$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun String.isChildPassenger(): Boolean {
    return try {
        val yearSubstring = this.substring(0, 2)
        val birthYear = if (yearSubstring.toInt() > 35) "19$yearSubstring".toInt() else "20${this.substring(0, 2)}".toInt()
        val birthMonth = this.substring(2, 4).toInt()
        val birthDay = this.substring(4, 6).toInt()

        calculateAge(birthYear, birthMonth, birthDay) < 15
    } catch (e: Exception) {
        false
    }
}

private fun calculateAge(year: Int, month: Int, day: Int): Int {
    val birthDate = Calendar.getInstance().apply {
        set(year, month - 1, day)
    }

    val currentDate = Calendar.getInstance()
    val years = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

    return if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
        (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))
    ) {
        years - 1
    } else {
        years
    }
}

fun String.showOnlyNumbers(): String {
    val regex = Regex("\\d+")
    return regex.findAll(this).map { it.value }.joinToString("")
}

fun String.showOnlyLetters(): String {
    val regex = Regex("[A-Za-z]+")
    return regex.findAll(this).map { it.value }.joinToString("")
}

@SuppressLint("SimpleDateFormat")
private fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {
    return try {
        val inputDateFormat = SimpleDateFormat(inputFormat)
        val outputDateFormat = SimpleDateFormat(outputFormat)
        val parsedDate = inputDateFormat.parse(dateToFormat)
        parsedDate?.let { outputDateFormat.format(it) }
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}