package kz.busjol.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
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
import kz.busjol.presentation.destinations.SearchJourneyScreenDestination
import kz.busjol.presentation.theme.Blue500

@ExperimentalAnimationApi
@Composable
fun BottomBar(
    navController: NavController,
    bottomBarState: Boolean = true,
    isPassengerMode: Boolean = true
) {
    val currentDestination: kz.busjol.presentation.destinations.Destination? =
        navController.currentBackStackEntryAsState()
            .value?.appDestination()

    val visibleState = MutableTransitionState(bottomBarState)

    val list = if (isPassengerMode) {
        listOf(
            BottomBarDestination.values()[0],
            BottomBarDestination.values()[1],
            BottomBarDestination.values()[2],
            BottomBarDestination.values()[3]
        )
    } else {
        listOf(
            BottomBarDestination.values()[4],
            BottomBarDestination.values()[5],
            BottomBarDestination.values()[3]
        )
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        BottomNavigation(
            backgroundColor = Color.White,
        ) {
            list.forEach { destination ->

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
    destination: BottomBarDestination,
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
