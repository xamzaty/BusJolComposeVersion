package kz.busjol.presentation.passenger.login

import kz.busjol.UserState


sealed interface LoginEvent {
    data class SetUserState(val value: UserState) : LoginEvent
}