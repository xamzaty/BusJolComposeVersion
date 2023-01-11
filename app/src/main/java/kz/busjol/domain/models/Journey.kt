package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.busjol.ext.formatWithCurrency
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

@Parcelize
data class Journey(
    val journey: JourneyItem?,
    val departureTime: String?,
    val arrivalTime: String?,
    val amount: Int?,
    val numberOfPlaces: Int?,
    val numberOfFreePlaces: Int?,
    val stopName: String?,
    val cityFrom: City?,
    val cityTo: City?,
    val segmentId: Int
): Parcelable {

    fun displayAmount() = amount?.formatWithCurrency()

    fun getDiffTime(hour: String, minute: String): String {
        val depTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+ss:SSS'Z'", Locale.ENGLISH).parse(departureTime ?: "")
        val arrTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm+ss:SSS'Z'", Locale.ENGLISH).parse(arrivalTime ?: "")

        val diff: Long = arrTime.time - depTime.time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return "$hours$hour $minutes$minute"
    }
}

@Parcelize
data class JourneyItem(
    val id: Int?,
    val created: String?,
    val status: Int?,
    val name: String?,
    val departsOn: String?,
    val routeId: Int?,
    val carrierId: Int?,
    val transportId: Int?,
    val code: String?,
    val driverId: Int?
): Parcelable