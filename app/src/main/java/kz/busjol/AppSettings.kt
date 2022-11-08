package kz.busjol

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language = Language.RUSSIAN,
    val userState: UserState = UserState.UNREGISTERED,
    var isNotificationsAvailable: Boolean = true,
    val email: String? = ""
)

enum class Language {
    RUSSIAN, KAZAKH
}

enum class UserState {
    UNREGISTERED, REGISTERED, DRIVER
}
