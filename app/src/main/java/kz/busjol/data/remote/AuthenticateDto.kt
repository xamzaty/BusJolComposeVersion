package kz.busjol.data.remote

class AuthenticateDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val jwtToken: String
)