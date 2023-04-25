package kz.busjol.presentation.passenger.buy_ticket.journey

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.domain.models.Journey
import kz.busjol.ext.reformatDateFromBackendOnlyTime
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.Loader
import kz.busjol.presentation.MultiStyleTextRow
import kz.busjol.presentation.NotFoundView
import kz.busjol.presentation.destinations.ChooseSeatsScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.BlueText
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.utils.timeLeft
import java.util.*

@Destination
@Composable
fun JourneyScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator,
    viewModel: JourneyViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.startNewDestination) {
        if (state.startNewDestination) {
            navigator.navigate(
                ChooseSeatsScreenDestination(
                    ticket = Ticket(
                        departureCity = ticket.departureCity,
                        arrivalCity = ticket.arrivalCity,
                        date = ticket.date,
                        journey = state.selectedJourney,
                        seatList = state.seatsList,
                        passengerList = ticket.passengerList
                    )
                )
            )
        }
    }

    DisposableEffect(state.startNewDestination) {
        onDispose {
            viewModel.onEvent(JourneyEvent.NewDestinationStatus(false))
        }
    }

    if (state.isLoading) {
        Loader(isDialogVisible = state.isLoading)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {
        AppBar(title = "${ticket.departureCity?.name} - ${ticket.arrivalCity?.name}") {
            scope.launch {
                navigator.navigateUp()
            }
        }

        if (ticket.journeyList.isNullOrEmpty()) {
            NotFoundView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 86.dp)
            )
        } else {
            RadioGroup(
                listOfOptions = listOf(
                    stringResource(id = R.string.radio_button_all),
                    stringResource(id = R.string.radio_button_seat),
                    stringResource(id = R.string.radio_button_lying)
                ),
                seatTypeText = R.string.radio_group_title,
                selectedOptionValue = state.selectedOption ?: 0
            )

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
                    .padding(top = 8.dp),
                content = {
                    item {
                        ticket.journeyList
                            .filteredList(seatTypeIndex = state.selectedOption)
                            .forEach { journey ->
                                JourneyItemView(journey) {
                                    scope.launch {
                                        viewModel.onEvent(
                                            JourneyEvent.OnJourneyClicked(
                                                id = journey.journey?.id.toString(),
                                                selectedJourney = journey
                                            )
                                        )
                                    }
                                }
                            }
                    }
                }
            )
        }
    }
}

@Composable
private fun JourneyItemView(
    journey: Journey,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        elevation = 1.dp,
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(top = 10.dp)
            .clickable { onClick() }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            val (busIcon, titleLayout, timeLeft, fromDetails, toDetails, bottomColumn, amount) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.bus),
                contentDescription = "bus",
                modifier = Modifier.constrainAs(busIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            Column(
                modifier = Modifier.constrainAs(titleLayout) {
                    top.linkTo(busIcon.top)
                    bottom.linkTo(busIcon.bottom)
                    start.linkTo(busIcon.end, 8.dp)
                }
            ) {
                Text(
                    text = stringResource(
                        id = R.string.journey_number,
                        journey.journey?.code ?: ""
                    ),
                    color = BlueText,
                    fontSize = 10.sp
                )

                Text(
                    text = "Автовокзал",
                    color = BlueText,
                    fontSize = 14.sp,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.constrainAs(timeLeft) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.time_left),
                    contentDescription = "timeLeft",
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = stringResource(
                        id = R.string.time_left,
                        timeLeft(journey.departureTime, journey.arrivalTime).first ?: "",
                        timeLeft(journey.departureTime, journey.arrivalTime).second ?: ""
                    ),
                    color = BlueText,
                    fontSize = 10.sp
                )
            }

            Column(
                modifier = Modifier.constrainAs(fromDetails) {
                    top.linkTo(busIcon.bottom, 12.dp)
                    start.linkTo(parent.start)
                }
            ) {
                Text(
                    text = journey.departureTime?.reformatDateFromBackendOnlyTime() ?: "",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = journey.cityFrom?.name ?: "",
                    fontSize = 16.sp,
                    color = BlueText,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                modifier = Modifier.constrainAs(toDetails) {
                    top.linkTo(fromDetails.top)
                    start.linkTo(fromDetails.end, 108.dp)
                }
            ) {
                Text(
                    text = journey.arrivalTime?.reformatDateFromBackendOnlyTime() ?: "",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = journey.cityTo?.name ?: "",
                    fontSize = 16.sp,
                    color = BlueText,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                modifier = Modifier.constrainAs(bottomColumn) {
                    top.linkTo(fromDetails.bottom, 14.dp)
                    start.linkTo(parent.start)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.seat_type, journey.stopName.toString()),
                    color = BlueText,
                    fontSize = 12.sp
                )

                MultiStyleTextRow(
                    text1 = stringResource(id = R.string.free_seats),
                    color1 = BlueText,
                    text2 = stringResource(
                        id = R.string.free_seats_of,
                        journey.numberOfFreePlaces.toString(), journey.numberOfPlaces.toString()
                    ),
                    color2 = Blue500,
                    fontSize = 12,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Card(
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color(0xFFF2F6F8),
                modifier = Modifier.constrainAs(amount) {
                    top.linkTo(bottomColumn.top)
                    end.linkTo(parent.end)
                }
            ) {
                Text(
                    text = journey.displayAmount() ?: "",
                    color = Blue500,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun List<Journey>.filteredList(seatTypeIndex: Int?): List<Journey> {
    return if (seatTypeIndex == 0) this
    else {
        val filterKey = seatTypeIndex!!.seatType().lowercase(Locale.getDefault())
        this.filter {
            it.stopName?.lowercase(Locale.getDefault())?.contains(filterKey) == true
        }
    }
}

@Composable
private fun Int.seatType(): String {
    val stringId = when (this) {
        0 -> R.string.radio_button_all
        1 -> R.string.radio_button_seat
        2 -> R.string.radio_button_lying
        else -> null
    }

    return stringId?.let { stringResource(id = it) } ?: ""
}


@Composable
fun RadioGroup(
    listOfOptions: List<String>,
    @StringRes seatTypeText: Int,
    selectedOptionValue: Int,
    viewModel: JourneyViewModel = hiltViewModel()
) {
    var selectedOption by rememberSaveable {
        mutableStateOf(selectedOptionValue)
    }

    val onSelectionChange = { position: Int ->
        selectedOption = position
        viewModel.onEvent(JourneyEvent.SelectedOption(position))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        Text(
            text = stringResource(id = seatTypeText),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(start = 15.dp, top = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 15.dp)
                .fillMaxWidth(),
        ) {
            itemsIndexed(listOfOptions) { index, text ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, GrayBorder),
                    elevation = 0.dp,
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                        .clickable {
                            onSelectionChange(index)
                        }
                ) {
                    Text(
                        text = text,
                        style = typography.body1.merge(),
                        textAlign = TextAlign.Center,
                        color = if (index == selectedOption) White else Color.Black,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (index == selectedOption) Blue500 else White)
                            .wrapContentHeight()
                    )
                }
            }
        }
    }
}
