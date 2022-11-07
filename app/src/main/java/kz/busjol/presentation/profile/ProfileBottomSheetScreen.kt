package kz.busjol.presentation.profile

sealed class ProfileBottomSheetScreen {
    object ChangeLanguageScreen : ProfileBottomSheetScreen()
    object RateTheAppScreen : ProfileBottomSheetScreen()
}