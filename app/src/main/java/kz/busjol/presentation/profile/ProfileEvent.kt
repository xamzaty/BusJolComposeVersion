package kz.busjol.presentation.profile

import kz.busjol.Language

sealed interface ProfileEvent {
    data class SetLanguage(val language: Language): ProfileEvent
}