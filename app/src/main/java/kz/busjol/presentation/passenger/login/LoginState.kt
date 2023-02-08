package kz.busjol.presentation.passenger.login

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val returnToMainScreen: Boolean = false
)
