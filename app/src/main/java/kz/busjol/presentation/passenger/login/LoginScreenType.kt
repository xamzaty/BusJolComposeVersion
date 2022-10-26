package kz.busjol.presentation.passenger.login

sealed interface LoginScreenType {
    object EnterScreen: LoginScreenType
    object PasswordRecoveryScreen: LoginScreenType
    object RegistrationScreen: LoginScreenType
}