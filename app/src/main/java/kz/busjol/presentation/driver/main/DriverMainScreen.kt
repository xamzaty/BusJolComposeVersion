package kz.busjol.presentation.driver.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kz.busjol.R
import kz.busjol.domain.models.Journey
import kz.busjol.presentation.Loader
import kz.busjol.presentation.NotFoundView
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.presentation.theme.GrayText
import kz.busjol.utils.showSnackBar

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun DriverMainScreen(
    viewModel: DriverMainViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    LaunchedEffect(state.error) {
        if (state.error != null) {
            scaffoldState.showSnackBar(this, state.error)
        }
     }
    
    if (state.isLoading) {
        Loader(isDialogVisible = true)
    }

    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, {
        viewModel.onEvent(
            DriverMainEvent.IsRefreshing
        )
    })

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(padding)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 15.dp, top = 22.dp)
            )

            if (state.journeyList?.isEmpty() == true) {
                NotFoundView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 86.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 6.dp,
                        start = 15.dp,
                        end = 15.dp,
                        bottom = 38.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 34.dp),
                    content = {
                        item {
                            state.journeyList?.forEach { list ->
                                DriverScheduleLayout(journey = list)
                            }
                        }
                    }
                )
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                contentColor = Blue500,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun DriverScheduleLayout(journey: Journey) {
    Card(
        elevation = 0.dp,
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            val (time, timeLeft, tripNumber, cities, placesFilled) = createRefs()

            Text(
                text = journey.departureTime ?: "12 сентября 12:00",
                color = GrayText,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(time) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )

            Text(
                text = "29:55",
                color = Blue500,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                modifier = Modifier.constrainAs(timeLeft) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )

            Text(
                text = "Рейс №23",
                fontSize = 10.sp,
                modifier = Modifier.constrainAs(tripNumber) {
                    start.linkTo(parent.start)
                    top.linkTo(time.bottom, 4.dp)
                }
            )

            Text(
                text = "${journey.cityFrom} - ${journey.cityTo}",
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                modifier = Modifier.constrainAs(cities) {
                    start.linkTo(parent.start)
                    top.linkTo(tripNumber.bottom, 4.dp)
                }
            )

            Text(
                text = "Заполнено 0 из 20",
                color = GrayText,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(placesFilled) {
                    start.linkTo(parent.start)
                    top.linkTo(cities.bottom, 4.dp)
                }
            )
        }
    }
}