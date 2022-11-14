package kz.busjol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kz.busjol.presentation.NavGraph
import kz.busjol.presentation.destinations.*
import kz.busjol.presentation.theme.BusJolComposeTheme
import kz.busjol.utils.BottomBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusJolComposeTheme {
                MainContent()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun MainContent(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val data = viewModel.userState.collectAsState(initial = AppSettings()).value
    val navGraph = remember { mutableStateOf(
        if (data.userState != UserState.DRIVER) passengerNavGraph else driverNavGraph
    ) }

    ShowBottomNavBar(navBackStackEntry = navBackStackEntry, bottomBarState = bottomBarState)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomBarState = bottomBarState.value,
                isPassengerMode =
                data.userState == UserState.UNREGISTERED || data.userState == UserState.REGISTERED
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            DestinationsNavHost(
                navGraph = navGraph.value,
                navController = navController
            )
        }
    }
}

@Composable
private fun ShowBottomNavBar(
    navBackStackEntry: NavBackStackEntry?,
    bottomBarState: MutableState<Boolean>
) {
    when (navBackStackEntry?.destination?.route) {
        "search_journey_screen", "my_tickets_screen", "contacts_screen", "profile_screen",
        "driver_main_screen", "passenger_verification_screen" ->
            bottomBarState.value = true
        else ->
            bottomBarState.value = false
    }
}

private val passengerNavGraph: NavGraph = NavGraph(
    route = "root",
    startRoute = SearchJourneyScreenDestination,
    destinations = listOf(
        BookingScreenDestination,
        ChooseSeatsScreenDestination,
        JourneyScreenDestination,
        PassengerDataScreenDestination,
        PaymentOrderScreenDestination,
        PaymentOrderResultScreenDestination,
        SearchJourneyScreenDestination,
        ContactsScreenDestination,
        FaqScreenDestination,
        LoginScreenDestination,
        PasswordRecoveryScreenDestination,
        RegistrationScreenDestination,
        MyTicketsScreenDestination,
        ProfileScreenDestination,
        MyDataScreenDestination,
        MyTripsScreenDestination,
        RateTheAppDestination
    )
)

private val driverNavGraph: NavGraph = NavGraph(
    route = "driverRoot",
    startRoute = DriverMainScreenDestination,
    destinations = listOf(
        SearchJourneyScreenDestination,
        DriverMainScreenDestination,
        PassengerVerificationScreenDestination,
        ScanScreenDestination,
        ProfileScreenDestination,
        MyTripsScreenDestination,
    )
)
