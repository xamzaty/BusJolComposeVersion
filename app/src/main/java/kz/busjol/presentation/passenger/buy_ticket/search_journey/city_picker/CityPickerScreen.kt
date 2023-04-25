package kz.busjol.presentation.passenger.buy_ticket.search_journey.city_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kz.busjol.R
import kz.busjol.domain.models.City
import kz.busjol.presentation.BackButton
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.Loader
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
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp, start = 15.dp, end = 15.dp, bottom = 15.dp)
                .fillMaxWidth()
        ) {
            BackButton(modifier = Modifier, onClick = onCloseBottomSheet)

            CustomTextField(
                text = text,
                onValueChange = { text = it },
                hintId = R.string.search_city_hint,
                labelId = R.string.search_city_label,
                iconId = R.drawable.search_24,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            )
        }

        Divider(modifier = Modifier.fillMaxWidth(), color = GrayBorder, thickness = 1.dp)

        when {
            state.isCityLoading -> Loader(isDialogVisible = state.isCityLoading)
            state.cityList.isNullOrEmpty() -> NotFoundView(
                modifier = Modifier.padding(top = 64.dp),
                textId = R.string.not_found
            )
            else -> LazyColumn {
                items(
                    items = state.cityList.filter { it.name?.lowercase()!!.contains(text.lowercase()) },
                    itemContent = { city ->
                        CityItem(city = city, lastCity = state.cityList.last()) {
                            if (fromOrToCity == "from") viewModel.onEvent(
                                SearchJourneyEvent.UpdateFromCityValue(city)
                            )
                            else viewModel.onEvent(SearchJourneyEvent.UpdateToCityValue(city))

                            onCloseBottomSheet()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CityItem(
    city: City,
    lastCity: City,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Text(
                text = city.name ?: "",
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        if (city != lastCity) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GrayBorder,
                thickness = 1.dp,
            )
        }
    }
}
