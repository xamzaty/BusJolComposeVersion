package kz.busjol.presentation.passenger.buy_ticket.search_journey

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Route
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City
import kz.busjol.ext.reformatDateToBackend
import kz.busjol.presentation.*
import kz.busjol.presentation.destinations.JourneyScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.search_journey.calendar.CalendarScreen
import kz.busjol.presentation.passenger.buy_ticket.search_journey.city_picker.CityPickerScreen
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.PassengerQuantityScreen
import kz.busjol.presentation.theme.GrayBorder

@Destination(start = true)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchJourneyScreen(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var currentBottomSheet: SearchJourneyBottomSheetScreen? by remember {
        mutableStateOf(null)
    }

    val closeSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        scope.launch { scaffoldState.bottomSheetState.collapse() }
    }

    val openSheet: (SearchJourneyBottomSheetScreen) -> Unit = {
        currentBottomSheet = it
        scope.launch { scaffoldState.bottomSheetState.expand() }

    }

    if (scaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = scaffoldState,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet, closeSheet)
            }
        }) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            MainContent(openSheet, navigator)
        }
    }
}

@Composable
fun SheetLayout(currentScreen: SearchJourneyBottomSheetScreen, onCloseBottomSheet: () -> Unit) {
    when (currentScreen) {
        is SearchJourneyBottomSheetScreen.PassengersScreen -> PassengerQuantityScreen(
            onCloseBottomSheet
        )
        is SearchJourneyBottomSheetScreen.CalendarScreen -> CalendarScreen(
            onCloseBottomSheet
        )
        is SearchJourneyBottomSheetScreen.CityPickerScreen -> CityPickerScreen(
            currentScreen.argument,
            onCloseBottomSheet
        )
    }
}

@Composable
private fun MainContent(
    openSheet: (SearchJourneyBottomSheetScreen) -> Unit,
    navigator: DestinationsNavigator,
    viewModel: SearchJourneyViewModel = hiltViewModel()
) {

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                viewModel.onEvent(SearchJourneyEvent.NewDestinationStatus(false))
            }
            else -> Unit
        }
    }

    val state = viewModel.state

    if (state.startNewDestination == true) {
        navigator.navigate(
            JourneyScreenDestination(
                ticket = Ticket(
                    departureCity = state.fromCity,
                    arrivalCity = state.toCity,
                    passengerList = state.passengerQuantityList,
                    journeyList = state.journeyList
                )
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 23.dp, start = 15.dp, end = 15.dp)
            .background(White)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.Black,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            )

            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 37.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.search_journey),
                    contentDescription = "searchJourney",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(id = R.string.search_journey_subtitle),
                    fontSize = 16.sp,
                    color = White,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }

            CitiesLayout(
                fromCityValue = state.fromCity?.name ?: "",
                toCityValue = state.toCity?.name ?: "",
                fromCitiesClick = {
                    viewModel.onEvent(
                        SearchJourneyEvent.CityListClicked
                    )
                    openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("from"))
                },

                toCitiesClick = {
                    viewModel.onEvent(
                        SearchJourneyEvent.CityListClicked
                    )
                    openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("to"))
                },

                swapButtonOnClick = {
                    viewModel.onEvent(
                        SearchJourneyEvent.SwapCities(
                            state.fromCity ?: City(), state.toCity ?: City()
                        )
                    )
                },
                modifier = Modifier.padding(top = 32.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                ClickableTextField(
                    text = state.arrivalDate ?: "",
                    iconId = R.drawable.calendar,
                    hintId = R.string.date_layout_label,
                    labelId = R.string.date_layout_label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 2.5.dp)
                ) {
                    openSheet(SearchJourneyBottomSheetScreen.CalendarScreen)
                }

                ClickableTextField(
                    text = state.passengerQuantity?.allPassengers()?.passengerText()
                        ?: stringResource(id = R.string.passenger_type_one, 1),
                    iconId = R.drawable.passenger,
                    hintId = R.string.passenger_layout_label,
                    labelId = R.string.passenger_layout_label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.5.dp)
                ) {
                    openSheet(SearchJourneyBottomSheetScreen.PassengersScreen)
                }
            }

            ProgressButton(
                textId = R.string.search_button,
                modifier = Modifier.padding(top = 32.dp),
                isProgressAvailable = state.isButtonLoading,
                isEnabled = true
            ) {
                viewModel.onEvent(
                    SearchJourneyEvent.JourneyListSearch(
                        JourneyPost(
                            cityFrom = state.fromCity?.id ?: 1,
                            cityTo = state.toCity?.id ?: 2,
                            dateFrom = state.departureDate?.reformatDateToBackend(true) ?: "",
                            dateTo = state.arrivalDate?.reformatDateToBackend(true) ?: "",
                            childrenAmount = state.passengerQuantity?.childValue ?: 0,
                            adultAmount = state.passengerQuantity?.adultValue ?: 1,
                            disabledAmount = state.passengerQuantity?.disabledValue ?: 0
                        )
                    )
                )
            }
        }
}

@Composable
fun CitiesLayout(
    fromCityValue: String,
    toCityValue: String,
    fromCitiesClick: () -> Unit,
    toCitiesClick: () -> Unit,
    swapButtonOnClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        ConstraintLayout {
            val (fromCityEt, toCityEt, swapButton, divider) = createRefs()

            CitiesTextField(
                value = fromCityValue,
                labelId = R.string.cities_layout_from_label,
                onClick = fromCitiesClick,
                modifier = Modifier.constrainAs(fromCityEt) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(divider.top)
                    width = Dimension.fillToConstraints
                })

            Divider(
                color = GrayBorder,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(divider) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            CitiesTextField(
                value = toCityValue,
                labelId = R.string.cities_layout_to_label,
                onClick = toCitiesClick,
                modifier = Modifier.constrainAs(toCityEt) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                })

            SwapCitiesButton(
                onClick = swapButtonOnClick,
                modifier = Modifier.constrainAs(swapButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 16.dp)
                })
        }
    }
}

@Composable
private fun CitiesTextField(
    value: String,
    @StringRes labelId: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = {},
        enabled = false,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle.Default.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = Color(0xFF444444)
        ),
        placeholder = { Text(text = stringResource(id = R.string.cities_layout_hint)) },
        label = { Text(text = stringResource(id = labelId)) },
        modifier = modifier
            .clickable { onClick() }
            .height(62.dp)
    )
}

@Composable
private fun SwapCitiesButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(52.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.swap_cities),
            contentDescription = "swapCities",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun Int.passengerText() = when (this) {
        1, 21, 31, 41, 51, 61, 71, 81, 91, 101 ->
            stringResource(id = R.string.passenger_type_one, this)
        in 2..4, in 22..24, in 32..34, in 42..44, in 52..54,
        in 62..64, in 72..74, in 82..84, in 92..94->
            stringResource(id = R.string.passenger_type_two, this)
        in 5..20, in 25..30, in 35..40, in 45..50, in 55..60,
        in 65..70, in 75..80, in 85..90, in 95..100 ->
            stringResource(id = R.string.passenger_type_three, this)
        else ->
            stringResource(id = R.string.passenger_type_one, this)
}