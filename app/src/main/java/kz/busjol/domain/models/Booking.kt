package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Booking(
    val id: Int,
    val qrCode: String,
    val seatNumber: String,
    val departsFrom: String,
    val arrivesTo: String,
    val departsAt: String,
    val arrivesAt: String,
    val clientInfo: String
): Parcelable
