package kz.busjol.presentation.passenger.search_journey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.presentation.CitiesLayout
import kz.busjol.presentation.ClickableTextField
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.passenger.city_picker.CityPickerScreen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchJourneyScreen() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var currentBottomSheet: SearchJourneyBottomSheetScreen? by remember{
        mutableStateOf(null)
    }

    val closeSheet: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    val openSheet: (SearchJourneyBottomSheetScreen) -> Unit = {
        currentBottomSheet = it
        scope.launch { scaffoldState.bottomSheetState.expand() }

    }
    if(scaffoldState.bottomSheetState.isCollapsed)
        currentBottomSheet = null

    BottomSheetScaffold(sheetPeekHeight = 0.dp, scaffoldState = scaffoldState,
        sheetContent = {
            currentBottomSheet?.let { currentSheet ->
                SheetLayout(currentSheet,closeSheet)
            }
        }) { paddingValues ->
        Box(Modifier.padding(paddingValues)){
            MainContent(openSheet)
        }
    }
}

@Composable
fun SheetLayout(currentScreen: SearchJourneyBottomSheetScreen, onCloseBottomSheet: () -> Unit) {
        when(currentScreen){
            SearchJourneyBottomSheetScreen.PassengersScreen -> {}
            is SearchJourneyBottomSheetScreen.CityPickerScreen -> CityPickerScreen(currentScreen.argument, onCloseBottomSheet)
        }
}

@Composable
private fun MainContent(openSheet: (SearchJourneyBottomSheetScreen) -> Unit) {
    var dateValue: String? = null

    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        datepicker { date ->
            dateValue = date.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 23.dp, start = 15.dp, end = 15.dp)
            .background(Color.White)
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
                color = Color.White,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }

        CitiesLayout(
            fromCityValue = "Алматы",
            toCityValue = "Балхаш",
            fromCitiesClick = { openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("from")) },
            toCitiesClick = { openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("to")) },
            swapButtonOnClick = {  },
            modifier = Modifier.padding(top = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            ClickableTextField(
                text = dateValue.orEmpty(),
                iconId = R.drawable.calendar,
                hintId = R.string.date_layout_label,
                labelId = R.string.date_layout_label,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.5.dp)
            ) {
                dialogState.show()
            }

            ClickableTextField(
                text = "",
                iconId = R.drawable.passenger,
                hintId = R.string.passenger_layout_label,
                labelId = R.string.passenger_layout_label,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.5.dp)
            ) {
                println("asdsdf")
            }
        }

        ProgressButton(
            textId = R.string.search_button,
            modifier = Modifier.padding(top = 32.dp),
            isProgressAvailable = false,
            isEnabled = true
        ) {

        }
    }
}
