package kz.busjol

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language? = null,
    val userState: UserState = UserState.UNREGISTERED,
    var isNotificationsAvailable: Boolean = true,
    val email: String? = "",
    val userData: UserData? = null
)

enum class Language(val value: String) {
    RUSSIAN("ru"), KAZAKH("kk")
}

enum class UserState {
    UNREGISTERED, REGISTERED, DRIVER
}

@Serializable
class UserData(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val jwtToken: String
)
