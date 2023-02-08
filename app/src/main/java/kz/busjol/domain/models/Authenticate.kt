package kz.busjol.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kz.busjol.ext.decodeToken

@Parcelize
data class Authenticate(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val jwtToken: String
): Parcelable {

    fun getToken() = jwtToken.decodeToken()
}
