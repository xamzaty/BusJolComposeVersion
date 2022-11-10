package kz.busjol.presentation.passenger.login

import kz.busjol.UserState

sealed interface LoginEvent {
    data class SetUserState(val value: UserState) : LoginEvent
    data class GetEmailValue(val value: String) : LoginEvent
    data class GetPasswordValue(val value: String) : LoginEvent
}