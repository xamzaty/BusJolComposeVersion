package kz.busjol.presentation.passenger.city_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kz.busjol.domain.models.City
import kz.busjol.presentation.BackButton
import kz.busjol.presentation.Loader
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun CityPickerScreen(
    fromOrToCity: String,
    onCloseBottomSheet: () -> Unit,
    viewModel: CityPickerViewModel = hiltViewModel()
) {
    viewModel.state.let { data ->
        
        Loader(isDialogVisible = data.isLoading)

        Column {
            Row(modifier = Modifier
                .padding(top = 16.dp, start = 15.dp, end = 15.dp)
                .fillMaxWidth()
            ) {
                BackButton(modifier = Modifier, isArrow = true) {
                    onCloseBottomSheet()
                }

                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 5.dp)
                        .height(30.dp)
                )
            }

            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = GrayBorder,
                thickness = 1.dp,
            )

            Loader(isDialogVisible = false)
            LazyColumn {
                item {
                    data.cityList?.forEach { city ->
                        CityItem(city, cityList = data.cityList) {
                            onCloseBottomSheet()
                        }
                    }
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
            text = item.name,
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
