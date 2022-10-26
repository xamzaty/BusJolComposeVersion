package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int? = null,
    val name: String? = null
): Parcelable