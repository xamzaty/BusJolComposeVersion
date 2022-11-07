package kz.busjol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kz.busjol.presentation.NavGraphs
import kz.busjol.presentation.driver.scan.ScanScreen
import kz.busjol.presentation.passenger.login.LoginScreen
import kz.busjol.presentation.passenger.login.password_recovery.PasswordRecoveryScreen
import kz.busjol.presentation.passenger.login.registration.RegistrationScreen
import kz.busjol.presentation.profile.ProfileScreen
import kz.busjol.presentation.profile.my_data.MyDataScreen
import kz.busjol.presentation.theme.BusJolComposeTheme
import kz.busjol.utils.BottomBar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusJolComposeTheme {
                val navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                ShowBottomNavBar(navBackStackEntry = navBackStackEntry, bottomBarState = bottomBarState)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(
                            navController = navController,
                            bottomBarState = true,
                            isPassengerMode = true
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowBottomNavBar(
    navBackStackEntry: NavBackStackEntry?,
    bottomBarState: MutableState<Boolean>
) {
    when (navBackStackEntry?.destination?.route) {
        "search_journey_screen", "my_tickets_screen", "contacts_screen", "profile_screen" -> bottomBarState.value = true
        else -> bottomBarState.value = false
    }
}
