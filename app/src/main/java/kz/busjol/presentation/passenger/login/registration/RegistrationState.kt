package kz.busjol.presentation.passenger.login.registration

data class RegistrationState(
    val isRegistrationSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
