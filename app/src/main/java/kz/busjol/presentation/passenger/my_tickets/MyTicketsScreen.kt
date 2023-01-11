package kz.busjol.presentation.passenger.my_tickets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.UserState
import kz.busjol.domain.models.Journey
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.destinations.LoginScreenDestination
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.BlueText
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.presentation.theme.GrayText

@Destination
@Composable
fun MyTicketsScreen(
    navigator: DestinationsNavigator,
    viewModel: MyTicketsViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    val isRegistered = remember {
        state.userState == UserState.REGISTERED
    }
    val isUserHaveTickets = remember { false }

    if (!isRegistered) {
        UnregisteredSection(scope, navigator)
    } else {
        RegisteredSection(isUserHaveTickets = isUserHaveTickets)
    }
}

@Composable
private fun UnregisteredSection(
    coroutineScope: CoroutineScope,
    navigator: DestinationsNavigator
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 22.dp, start = 15.dp, end = 15.dp, bottom = 16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.my_tickets_title),
            fontWeight = FontWeight.W700,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.bus_with_city_image),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 86.dp)
                .size(128.dp)
        )

        Text(
            text = stringResource(id = R.string.my_tickets_description_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.my_tickets_description_text),
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .width(200.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        ProgressButton(
            textId = R.string.enter_button,
            isProgressBarActive = false,
            enabled = true
        ) {
            coroutineScope.launch {
                navigator.navigate(
                    LoginScreenDestination()
                )
            }
        }
    }
}

@Composable
private fun RegisteredSection(isUserHaveTickets: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 22.dp, start = 15.dp, end = 15.dp, bottom = 16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.my_tickets_title),
            fontWeight = FontWeight.W700,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isUserHaveTickets) {
            Image(
                painter = painterResource(id = R.drawable.bus_with_city_image),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 86.dp)
                    .size(128.dp)
            )

            Text(
                text = stringResource(id = R.string.my_tickets_description_text_no_tickets),
                fontSize = 15.sp,
                color = GrayText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 20.dp)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 38.dp,
                    bottom = 109.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                content = {
                    item {
//                        journeyList.forEach {
//                            TicketItemView(journey = it) {
//
//                            }
//                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TicketItemView(
    modifier: Modifier = Modifier,
    journey: Journey? = null,
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
                    text = stringResource(id = R.string.journey_number, journey?.journey?.code ?: ""),
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
                    text = stringResource(id = R.string.time_left, "13", "20"),
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
                    text = journey?.departureTime ?: "",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = journey?.cityFrom?.name ?: "",
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
                    text = journey?.arrivalTime ?: "",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = journey?.cityTo?.name ?: "",
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
                    text = stringResource(id = R.string.seat_type, "Сидячий"),
                    color = BlueText,
                    fontSize = 12.sp
                )
            }

            Card(
                elevation = 0.dp,
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color(0xFFF2F6F8),
                modifier = Modifier.constrainAs(amount) {
                    top.linkTo(bottomColumn.top)
                    bottom.linkTo(bottomColumn.bottom)
                    end.linkTo(parent.end)
                }
            ) {
                Text(
                    text = journey?.displayAmount() ?: "",
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
