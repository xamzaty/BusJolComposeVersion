package kz.busjol.presentation.passenger.login.registration

import kz.busjol.data.remote.RegisterPost

sealed interface RegistrationEvent {
    data class OnRegisterButtonClicked(val body: RegisterPost) : RegistrationEvent
}