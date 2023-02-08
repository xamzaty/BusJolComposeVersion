package kz.busjol.presentation.passenger.login

import kz.busjol.UserState
import kz.busjol.data.remote.AuthenticatePost

sealed interface LoginEvent {
    data class OnLoginButtonPressed(val authenticatePost: AuthenticatePost) : LoginEvent
}