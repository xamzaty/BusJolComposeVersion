package kz.busjol.presentation.passenger.buy_ticket.search_journey.city_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kz.busjol.domain.models.City
import kz.busjol.presentation.BackButton
import kz.busjol.presentation.NotFoundView
import kz.busjol.presentation.passenger.buy_ticket.search_journey.SearchJourneyEvent
import kz.busjol.presentation.passenger.buy_ticket.search_journey.SearchJourneyViewModel
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun CityPickerScreen(
    fromOrToCity: String,
    onCloseBottomSheet: () -> Unit,
    viewModel: SearchJourneyViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
                .fillMaxWidth()
        ) {

            BackButton(modifier = Modifier) {
                onCloseBottomSheet()
            }

            TextField(
                value = text,
                textStyle = MaterialTheme.typography.body1.copy(color = Color.Black),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = White,
                ),
                onValueChange = { cityValue ->
                    text = cityValue
                },
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            )
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = GrayBorder,
            thickness = 1.dp
        )

        LazyColumn {
            item {
                if (state.cityList?.isNotEmpty() == true) {
                    state.cityList.filter {
                        it.name?.lowercase()!!.contains(text.text.lowercase())
                    }.forEach { city ->
                        CityItem(city, cityList = state.cityList) {
                            if (fromOrToCity == "from") viewModel.onEvent(SearchJourneyEvent.UpdateFromCityValue(
                                    city
                                )
                            )
                            else viewModel.onEvent(SearchJourneyEvent.UpdateToCityValue(city))

                            onCloseBottomSheet()
                        }
                    }
                } else {
                    NotFoundView(modifier = Modifier.padding(25.dp))
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    item: City,
    cityList: List<City>,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(45.dp)
    ) {
        Text(
            text = item.name ?: "",
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.CenterVertically)
        )
    }
    if (item != cityList.last()) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = GrayBorder,
            thickness = 1.dp,
        )
    }
}
