package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.busjol.ext.formatWithCurrency

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
    val cityTo: City?
): Parcelable {

    fun displayAmount() = amount?.formatWithCurrency()
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
    val code: String?
): Parcelable