package kz.busjol.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun timeLeft(
    departureDate: String?,
    arrivalDate: String?,
): Pair<String?, String?> {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val dateDeparture = departureDate?.let { sdf.parse(it) }
        val dateArrival = arrivalDate?.let { sdf.parse(it) }

        if (dateDeparture != null && dateArrival != null) {
            val millis = dateDeparture.time - dateArrival.time

            val minutes = (millis / (1000 * 60)) % 60
            val hours = millis / (1000 * 60 * 60)

            Pair(
                hours.toString().replace("-", ""),
                minutes.toString().replace("-", "")
            )
        } else {
            Pair(null, null)
        }
    } catch (e: ParseException) {
        Pair(e.toString(), e.toString())
    }
}
