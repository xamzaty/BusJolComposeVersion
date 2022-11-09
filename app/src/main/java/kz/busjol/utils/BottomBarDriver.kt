import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.get
import com.ramcosta.composedestinations.navigation.navigate
import kz.busjol.presentation.appDestination
import kz.busjol.presentation.destinations.Destination
import kz.busjol.presentation.destinations.SearchJourneyScreenDestination
import kz.busjol.presentation.theme.Blue500
import kz.busjol.utils.BottomBarDestination
import kz.busjol.utils.BottomBarDestinationDriver

@ExperimentalAnimationApi
@Composable
fun BottomBarDriver(
    navController: NavController,
    bottomBarState: Boolean = true
) {
    val currentDestination: Destination? =
        navController.currentBackStackEntryAsState()
            .value?.appDestination()

    val visibleState = MutableTransitionState(bottomBarState)

    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
        ) {
            BottomBarDestinationDriver.values().forEach { destination ->

                BottomNavigationItem(
                    selected = currentDestination == destination.direction,
                    onClick = {
                        navController.navigate(destination.direction, fun NavOptionsBuilder.() {
                            popUpTo(navController.graph[SearchJourneyScreenDestination.route].id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        })
                    },
                    icon = {
                        NavigationItemIcon(
                            destination = destination,
                            selected = currentDestination == destination.direction,
                            text = stringResource(id = destination.label)
                        )
                    },
                    unselectedContentColor = Color.White,
                    selectedContentColor = Blue500
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun NavigationItemIcon(
    destination: BottomBarDestinationDriver,
    selected: Boolean,
    text: String
) {
    AnimatedContent(targetState = selected) { target ->

        val iconTintColor = remember {
            mutableStateOf(if (target) Blue500 else Color(0xFF8B98A7))
        }

        val textColor = remember {
            mutableStateOf(if (target) Blue500 else Color(0xFF8B98A7))
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = destination.icon),
                contentDescription = "",
                modifier = Modifier.size(23.dp, 23.dp),
                tint = iconTintColor.value
            )

            Text(
                text = text,
                color = textColor.value,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}