package kz.busjol.presentation.profile

import kz.busjol.Language
import kz.busjol.UserState

data class ProfileState(
    val language: Language? = null,
    val userState: UserState? = null,
    val isNotificationsEnabled: Boolean = true,
    val emailValue: String? = null
)
