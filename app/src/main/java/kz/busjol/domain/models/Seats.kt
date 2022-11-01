package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Seats(
    val row: Int,
    val column: Int,
    val seatNumber: String,
    val isEmpty: Boolean,
    val id: Int,
    val created: String,
    val status: Int,
    val name: String
) : Parcelable
