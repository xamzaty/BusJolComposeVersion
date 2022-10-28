package kz.busjol.presentation.passenger.search_journey.passenger_quantity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kz.busjol.R
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.passenger.search_journey.SearchJourneyEvent
import kz.busjol.presentation.passenger.search_journey.SearchJourneyViewModel
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun PassengerQuantityScreen(
    onCloseBottomSheet: () -> Unit,
    viewModel: SearchJourneyViewModel = hiltViewModel()
) {
    viewModel.state.let { data ->

        val passengersQuantity = remember { data.passengerQuantity }

        val adultQuantity = remember { passengersQuantity?.adultValue ?: 1 }
        val childQuantity = remember { passengersQuantity?.childValue ?: 0 }
        val disabledQuantity = remember { passengersQuantity?.disabledValue ?: 0 }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "close",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 15.dp)
                        .clickable { onCloseBottomSheet() }
                )

                Text(text = stringResource(id = R.string.passenger_quantity_screen_title),
                    fontSize = 20.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = GrayBorder,
                thickness = 1.dp
            )

            PassengerLayout(
                passengerType = stringResource(id = R.string.adult_passenger),
                description = stringResource(id = R.string.adult_passenger_description),
                modifier = Modifier.padding(top = 16.dp, start = 15.dp, end = 15.dp),
                quantity = adultQuantity
            )

            PassengerLayout(
                passengerType = stringResource(id = R.string.child_passenger),
                description = stringResource(id = R.string.child_passenger_description),
                modifier = Modifier.padding(top = 16.dp, start = 15.dp, end = 15.dp),
                quantity = childQuantity
            )

            PassengerLayout(
                passengerType = stringResource(id = R.string.disabled_person),
                description = stringResource(id = R.string.disabled_person_passenger_description),
                modifier = Modifier.padding(top = 16.dp, start = 15.dp, end = 15.dp),
                quantity = disabledQuantity
            )

            ProgressButton(
                textId = R.string.choose_button,
                modifier = Modifier.padding(top = 24.dp, start = 15.dp, end = 15.dp, bottom = 24.dp),
                isProgressAvailable = false,
                isEnabled = true
            ) {
                val adultList = List(adultQuantity) { listOf(Passenger(Passenger.PassengerType.ADULT)) }.flatten()
                val childList = List(childQuantity) { listOf(Passenger(Passenger.PassengerType.CHILD)) }.flatten()
                val disabledList = List(disabledQuantity) { listOf(Passenger(Passenger.PassengerType.DISABLED)) }.flatten()

                val listOfAllPassengers : List<Passenger> = merge(adultList, childList, disabledList)

                viewModel.onEvent(SearchJourneyEvent.UpdatePassengersQuantityValue(listOfAllPassengers))
                println(listOfAllPassengers)
                onCloseBottomSheet()
            }
        }
    }
}

@Composable
private fun PassengerLayout(
    passengerType: String,
    description: String,
    quantity: Int,
    modifier: Modifier
) {

    var quantityValue by rememberSaveable { mutableStateOf(quantity) }

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, GrayBorder),
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.5.dp, bottom = 8.5.dp, end = 16.dp)
        ) {

            Column {
                Text(
                    text = passengerType,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp),
                    color = Color(0xFF8B98A7),
                    fontWeight = FontWeight.W400
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .width(80.dp)
                    .padding(top = 8.5.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.minus_button),
                    contentDescription = "minus",
                    modifier = Modifier.clickable {
                        if (quantityValue > 0) quantityValue--
                    }
                )

                Text(
                    text = quantityValue.toString(),
                    fontSize = 15.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Image(
                    painter = painterResource(id = R.drawable.plus_button),
                    contentDescription = "plus",
                    modifier = Modifier.clickable { quantityValue++ }
                )
            }
        }
    }
}

private fun <T> merge(first: List<T>, second: List<T>, third: List<T>): List<T> {
    return first + second + third
}
