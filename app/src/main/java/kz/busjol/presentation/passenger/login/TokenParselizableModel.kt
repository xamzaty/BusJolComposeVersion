package kz.busjol.presentation.passenger.login

@kotlinx.serialization.Serializable
data class TokenParseModel(
    val id: String? = null,
    val role: String? = null,
    val nbf: Long? = null,
    val exp: Long? = null,
    val iat: Long? = null
) {

    fun isDriver() = role?.contains("driver")
}
