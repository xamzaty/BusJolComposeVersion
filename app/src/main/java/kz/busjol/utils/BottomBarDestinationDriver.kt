package kz.busjol.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import kz.busjol.R
import kz.busjol.presentation.destinations.DriverMainScreenDestination
import kz.busjol.presentation.destinations.ProfileScreenDestination
import kz.busjol.presentation.destinations.ScanScreenDestination

enum class BottomBarDestinationDriver(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    DriverMain(DriverMainScreenDestination, R.drawable.profile_category, R.string.driver_main_category),
    Scan(ScanScreenDestination, R.drawable.profile_category, R.string.scan_category),
    Profile(ProfileScreenDestination, R.drawable.profile_category, R.string.profile_category)
}