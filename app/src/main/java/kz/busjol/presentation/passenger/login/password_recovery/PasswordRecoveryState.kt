package kz.busjol.presentation.passenger.login.password_recovery

data class PasswordRecoveryState(
    val isPasswordRecoverySuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)