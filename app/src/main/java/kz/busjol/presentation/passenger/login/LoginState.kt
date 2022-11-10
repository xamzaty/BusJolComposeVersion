package kz.busjol.presentation.passenger.login

data class LoginState(
    val isButtonEnable: Boolean? = false,
    val emailValue: String? = null,
    val passwordValue: String? = null,
)
