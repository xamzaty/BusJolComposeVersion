package kz.busjol.presentation.passenger.login.password_recovery

import kz.busjol.data.remote.RestorePasswordPost

sealed interface PasswordRecoveryEvent {
    data class OnSendButtonClicked(val body: RestorePasswordPost) : PasswordRecoveryEvent
}