package kz.busjol.presentation.profile

import kz.busjol.Language

sealed interface ProfileEvent {
    data class SetLanguage(val language: Language): ProfileEvent
    data class SetNotificationStatus(val notificationStatus: Boolean): ProfileEvent
    object ExitUserState: ProfileEvent
}