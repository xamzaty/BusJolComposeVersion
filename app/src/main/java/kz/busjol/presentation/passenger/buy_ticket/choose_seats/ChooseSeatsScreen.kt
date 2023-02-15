package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.presentation.AppBar
import kz.busjol.R
import kz.busjol.domain.models.Seats
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.destinations.PassengerDataScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.Passenger
import kz.busjol.presentation.theme.*
import kz.busjol.utils.rememberViewInteropNestedScrollConnection
import kz.busjol.utils.showSnackBar

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun ChooseSeatsScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator,
    viewModel: ChooseSeatsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    val isButtonEnabled by remember {
        mutableStateOf(state.seatsQuantity == state.passengersQuantity)
    }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(ticket.passengerList) {
        viewModel.onEvent(
            ChooseSeatsEvent.PassPassengersQuantity(
                ticket.passengerList?.size ?: 1
            )
        )
    }

    Scaffold(scaffoldState = scaffoldState) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBackground)
                .padding(padding),
            content = {

                stickyHeader {
                    AppBar(title = stringResource(id = R.string.choose_seats_title)) {
                        scope.launch {
                            navigator.navigateUp()
                        }
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(30.dp)
                    ) {
                        SmallDescriptionBox(
                            isFreeSeat = true, modifier = Modifier.padding(end = 8.dp)
                        )

                        Text(
                            text = stringResource(id = R.string.free_seat),
                            color = Color.Black,
                            fontSize = 14.sp
                        )

                        SmallDescriptionBox(
                            isFreeSeat = false,
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                        )

                        Text(
                            text = stringResource(id = R.string.occupied_seat),
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }

                item {
                    Card(
                        elevation = 0.dp,
                        border = BorderStroke(1.dp, GrayBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .nestedScroll(rememberViewInteropNestedScrollConnection())
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 60.dp, end = 60.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                start = 24.dp, top = 24.dp, end = 24.dp, bottom = 44.dp
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.wheel),
                                contentDescription = "wheel",
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .size(38.dp)
                            )

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                mainAxisSize = SizeMode.Wrap,
                                mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween
                            ) {
                                ticket.seatList?.forEachIndexed { index, seats ->
                                    SeatItem(
                                        seats = seats,
                                        modifier = Modifier.seatModifier(index)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 31.dp)
                            .offset(y = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 15.dp)
                        ) {

                            Row {
                                Text(
                                    text = stringResource(id = R.string.chosen_seats),
                                    color = GrayText,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(end = 5.dp)
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = state.chosenSeatsList ?: "",
                                    fontWeight = FontWeight.W500,
                                    fontSize = 15.sp
                                )
                            }

                            val passengerList = state.seatList?.toNewPassengerList(ticket.passengerList ?: emptyList())

                            ProgressButton(
                                textId = R.string.continue_button,
                                isProgressBarActive = false,
                                enabled = isButtonEnabled,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                scope.launch {
                                    if (state.seatsQuantity != state.passengersQuantity) {
                                        scaffoldState.showSnackBar(
                                            this,
                                            "Выберите нужное количество сидении"
                                        )
                                    } else {
                                        navigator.navigate(
                                            PassengerDataScreenDestination(
                                                ticket = Ticket(
                                                    departureCity = ticket.departureCity,
                                                    arrivalCity = ticket.arrivalCity,
                                                    date = ticket.date,
                                                    passengerList = passengerList,
                                                    journey = ticket.journey,
                                                    chosenSeatsList = state.seatList
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SeatItem(
    modifier: Modifier = Modifier,
    seats: Seats,
    viewModel: ChooseSeatsViewModel = hiltViewModel()
) {
    val isChecked = rememberSaveable { mutableStateOf(false) }
    val border = remember { mutableStateOf(BorderStroke(2.dp, Blue500)) }
    val backgroundColor = remember { mutableStateOf(Color.White) }
    val textColor = remember { mutableStateOf(Color.Black) }

    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        backgroundColor = if (seats.isEmpty) backgroundColor.value else GrayBackground,
        border = if (seats.isEmpty) border.value else BorderStroke(2.dp, GrayBackground),
        modifier = modifier
            .size(40.dp)
            .toggleable(value = isChecked.value, role = Role.Checkbox) {
                if (seats.isEmpty) {
                    isChecked.value = it

                    if (isChecked.value) {
                        viewModel.onEvent(ChooseSeatsEvent.AddItemToList(seats))
                        border.value = BorderStroke(2.dp, Blue500)
                        backgroundColor.value = Blue500
                        textColor.value = Color.White
                    } else {
                        viewModel.onEvent(ChooseSeatsEvent.RemoveItem(seats))
                        border.value = BorderStroke(2.dp, Blue500)
                        backgroundColor.value = Color.White
                        textColor.value = Color.Black
                    }
                }
            },

        content = {
            Text(
                text = seats.seatNumber,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = if (seats.isEmpty) textColor.value else Color.Black,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
            )
        }
    )
}

@Composable
private fun SmallDescriptionBox(
    isFreeSeat: Boolean,
    modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        backgroundColor = if (isFreeSeat) Color.White else GrayBackground,
        border = if (isFreeSeat) BorderStroke(1.dp, Blue500) else BorderStroke(
            0.dp,
            DarkGrayBackground
        ),
        modifier = modifier
            .size(16.dp),
        content = { })
}

private fun Modifier.newSeatModifier(row: Int, column: Int) = when {
    row == 1 && column == 1 -> this.padding(top = 24.dp)
    row == 1 && column == 2 -> this.padding(start = 8.dp, top = 24.dp, end = 15.dp)
    row == 1 && column == 3 -> this.padding(start = 20.dp, top = 24.dp)
    row == 1 && column == 4 -> this.padding(start = 8.dp, top = 24.dp)

    else -> this
}

private fun Modifier.seatModifier(index: Int) = when {
    index == 0 -> this.padding(top = 24.dp)
    index == 1 -> this.padding(start = 8.dp, top = 24.dp, end = 15.dp)
    index == 2 -> this.padding(start = 20.dp, top = 24.dp)
    index == 3 -> this.padding(start = 8.dp, top = 24.dp)
    index % 4 == 0 -> this.padding(top = 8.dp)
    index % 4 == 1 -> this.padding(start = 8.dp, top = 8.dp, end = 15.dp)
    index % 4 == 2 -> this.padding(start = 20.dp, top = 8.dp)
    index % 4 == 3 -> this.padding(start = 8.dp, top = 8.dp)
    else -> this
}

private fun List<Seats>.toNewPassengerList(list: List<Passenger>): List<Passenger> {
    val convertedData: ArrayList<Passenger> = ArrayList()

    if (this.size != list.size) {
        return convertedData
    }

    this.forEachIndexed { index, seat ->
        val passenger = list[index]

        val newPassenger = Passenger(
            type = passenger.type,
            iin = passenger.iin,
            lastName = passenger.lastName,
            firstName = passenger.firstName,
            seatId = seat.id
        )
        convertedData.add(newPassenger)
    }

    return convertedData
}
