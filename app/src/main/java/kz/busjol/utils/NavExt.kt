package kz.busjol.utils

import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kz.busjol.presentation.NavGraphs
import kz.busjol.presentation.destinations.DirectionDestination
import kz.busjol.presentation.destinations.DriverMainScreenDestination
import kz.busjol.presentation.destinations.SearchJourneyScreenDestination
import kz.busjol.presentation.startAppDestination

fun DestinationsNavigator.backToMainScreen() =
    this.navigate(NavGraphs.root.startAppDestination as DirectionDestination) {
        popUpTo(SearchJourneyScreenDestination) {
            inclusive = true
        }
    }

fun DestinationsNavigator.backToDriverMainScreen() =
    this.navigate(NavGraphs.root.startAppDestination as DirectionDestination) {
        popUpTo(DriverMainScreenDestination) {
            inclusive = true
        }
    }