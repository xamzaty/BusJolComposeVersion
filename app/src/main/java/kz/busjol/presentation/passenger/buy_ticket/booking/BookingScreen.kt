package kz.busjol.presentation.passenger.buy_ticket.booking

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.presentation.passenger.buy_ticket.journey_details.JourneyDetailsScreen
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.R
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kz.busjol.ext.formatWithCurrency
import kz.busjol.presentation.*
import kz.busjol.presentation.destinations.DirectionDestination
import kz.busjol.presentation.destinations.PaymentOrderResultScreenDestination
import kz.busjol.presentation.destinations.SearchJourneyScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.presentation.theme.GrayText

@Destination
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookingScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent =
        {
            JourneyDetailsScreen(sheetState, coroutineScope, ticket)
        }) {
        MainContent(sheetState, coroutineScope, ticket, navigator)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    ticket: Ticket,
    navigator: DestinationsNavigator,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val openDialog = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {

        BackHandler {
            openDialog.value = true
        }

        AppBar(title = stringResource(id = R.string.booking_title)) {
            openDialog.value = true
        }

        if (openDialog.value) {
            CustomAlertDialog(
                openDialog = openDialog.value,
                title = stringResource(id = R.string.alert_exit_title),
                description = stringResource(id = R.string.alert_exit_description),
                confirmButtonText = stringResource(id = R.string.yes),
                dismissButtonText = stringResource(id = R.string.no),
                onConfirmButtonClicked = {
                    openDialog.value = false
                    navigator.backToMainScreen()
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 15.dp, top = 16.dp, end = 15.dp)
        ) {

            Text(
                text = stringResource(id = R.string.pay_for_the_reservation),
                fontSize = 15.sp,
                color = Color(0xFF8B98A7)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = state.countdownTimerValue ?: "10:00",
                fontSize = 17.sp,
                color = Color.Black,
                fontWeight = FontWeight.W700
            )
        }

        Card(
            elevation = 0.dp,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 15.dp, end = 15.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {

                Text(
                    text = stringResource(id = R.string.journey_number, "23"),
                    fontSize = 11.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "Алматы - Балхаш",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.W700,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(id = R.string.passenger_data_details),
                        color = Blue500,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable {
                            coroutineScope.launch { sheetState.show() }
                        }
                    )
                }

                Text(
                    text = "24.12.2022 09:00",
                    color = GrayText,
                    modifier = Modifier.padding(
                        top = 4.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W500
                )

                val amountValue by rememberSaveable { mutableStateOf(10000) }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blue500)
                        .padding(top = 10.dp, bottom = 10.dp)
                ) {

                    Text(
                        text = amountValue.formatWithCurrency(),
                        fontWeight = FontWeight.W700,
                        color = Color.White,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }

        PaymentTypeLayout(
            isBankCardsPayment = true,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            coroutineScope.launch {
                navigator.navigate(
                    PaymentOrderResultScreenDestination(
                        ticket
                    )
                )
            }
        }

        PaymentTypeLayout(
            isBankCardsPayment = false,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            coroutineScope.launch {
                navigator.navigate(
                    PaymentOrderResultScreenDestination(
                        ticket
                    )
                )
            }
        }
    }
}

@Composable
private fun PaymentTypeLayout(
    modifier: Modifier = Modifier,
    isBankCardsPayment: Boolean,
    onClick: () -> Unit
) {

    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GrayBorder),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = if (isBankCardsPayment) R.drawable.bank_card else R.drawable.kaspi_logo),
                    contentDescription = "bank",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = stringResource(id = if (isBankCardsPayment) R.string.with_bank_cards else R.string.with_kaspi),
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            ProgressButton(
                textId = R.string.payment_button,
                isProgressBarActive = false,
                enabled = true,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .height(42.dp)
            ) {
                onClick()
            }
        }
    }
}

@Composable
private fun RoundedCheckView(
    modifier: Modifier = Modifier,
    isCheckedValue: Boolean,
) {
    val isChecked = remember { mutableStateOf(isCheckedValue) }
    val backgroundColor = remember { mutableStateOf(Color.White) }

    Card(
        elevation = 0.dp,
        shape = CircleShape,
        border = BorderStroke(1.dp, Blue500),
        backgroundColor = backgroundColor.value,
        modifier = modifier
            .size(20.dp)
            .toggleable(value = isChecked.value, role = Role.Checkbox) {
                isChecked.value = it

                if (isChecked.value) {
                    backgroundColor.value = Blue500
                } else {
                    backgroundColor.value = Color.White
                }
            }
    ) {
        if (isChecked.value) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

private fun DestinationsNavigator.backToMainScreen() =
    this.navigate(NavGraphs.root.startAppDestination as DirectionDestination) {
        popUpTo(SearchJourneyScreenDestination) {
            inclusive = true
        }
    }
