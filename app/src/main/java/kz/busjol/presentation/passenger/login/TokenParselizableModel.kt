package kz.busjol.presentation.passenger.login

@kotlinx.serialization.Serializable
data class TokenParseModel(
    val id: String,
    val role: String,
    val nbf: Long,
    val exp: Long,
    val iat: Long
) {

    fun isDriver() = role.contains("driver")
}
