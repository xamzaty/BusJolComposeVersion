package kz.busjol.presentation.passenger.buy_ticket.booking

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.passenger.buy_ticket.journey_details.JourneyDetailsScreen
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kz.busjol.ext.formatWithCurrency
import kz.busjol.presentation.ProgressButton
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
            JourneyDetailsScreen(sheetState, coroutineScope)
        }) {
        MainContent(sheetState, coroutineScope)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {

    val timerValue by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {

        AppBar(
            title = stringResource(
                id = R.string.booking_title
            )
        ) {

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
                text = "10:00",
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
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp ),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 16.dp, end = 16.dp,)
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
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
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
            checkboxState = true,
            modifier = Modifier.padding(top = 16.dp)
        ) {

        }

        PaymentTypeLayout(
            isBankCardsPayment = false,
            checkboxState = false,
            modifier = Modifier.padding(top = 12.dp)
        ) {

        }
    }
}

@Composable
private fun PaymentTypeLayout(
    modifier: Modifier = Modifier,
    isBankCardsPayment: Boolean,
    checkboxState: Boolean,
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

                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = stringResource(id = if (isBankCardsPayment) R.string.with_bank_cards else R.string.with_kaspi),
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 15.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            if (checkboxState) {
                ProgressButton(
                    textId = R.string.payment_button,
                    isProgressAvailable = false,
                    isEnabled = true,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .height(42.dp)
                ) {
                    onClick()
                }
            }
        }
    }
}
