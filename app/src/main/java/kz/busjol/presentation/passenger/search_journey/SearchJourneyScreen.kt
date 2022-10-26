package kz.busjol.presentation.passenger.search_journey

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.City
import kz.busjol.presentation.*
import kz.busjol.presentation.passenger.search_journey.city_picker.CityPickerScreen
import kz.busjol.presentation.passenger.search_journey.passenger_quantity.PassengerQuantityScreen
import kz.busjol.presentation.theme.GrayBorder
import java.text.SimpleDateFormat
import java.util.*

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

    BackHandler(scaffoldState.bottomSheetState.isExpanded) {
        scope.launch { scaffoldState.bottomSheetState.collapse() }
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
            SearchJourneyBottomSheetScreen.PassengersScreen -> PassengerQuantityScreen(onCloseBottomSheet)
            is SearchJourneyBottomSheetScreen.CityPickerScreen -> CityPickerScreen(currentScreen.argument, onCloseBottomSheet)
        }
}

@Composable
private fun MainContent(
    openSheet: (SearchJourneyBottomSheetScreen) -> Unit,
    viewModel: SearchJourneyViewModel = hiltViewModel()
) {
    viewModel.state.let { data ->
        
//        Loader(isDialogVisible = data.isLoading)

        println("journeyList: ${data.journeyList}")

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val currentDate = sdf.format(Date())

        var dateValue = remember { currentDate }

        val dialogState = rememberMaterialDialogState()
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Ok")
                negativeButton("Отмена")
            }
        ) {
            datepicker { date ->
                dateValue = date.toString()
            }
        }

        val fromCity = viewModel.state.fromCity ?: City(0, "")
        val toCity = viewModel.state.toCity ?: City(0, "")

        val isAllFieldsNotEmpty = remember {
            fromCity.name?.isEmpty() == true && toCity.name?.isEmpty() == true
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
                fromCityValue = fromCity.name ?: "",
                toCityValue = toCity.name ?: "",
                fromCitiesClick = {
                    viewModel.onAction(SearchJourneyAction.CityListClicked)
                    openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("from")) },
                toCitiesClick = {
                    viewModel.onAction(SearchJourneyAction.CityListClicked)
                    openSheet(SearchJourneyBottomSheetScreen.CityPickerScreen("to")) },
                swapButtonOnClick = {
                    viewModel.onAction(
                        SearchJourneyAction.SwapCities(fromCity, toCity)
                    ) },
                modifier = Modifier.padding(top = 32.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                ClickableTextField(
                    text = dateValue,
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
                    text = "1 пассажир",
                    iconId = R.drawable.passenger,
                    hintId = R.string.passenger_layout_label,
                    labelId = R.string.passenger_layout_label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 2.5.dp)
                ) {
                    openSheet(SearchJourneyBottomSheetScreen.PassengersScreen)
                }
            }

            ProgressButton(
                textId = R.string.search_button,
                modifier = Modifier.padding(top = 32.dp),
                isProgressAvailable = data.isButtonLoading,
                isEnabled = isAllFieldsNotEmpty
            ) {
                viewModel.onAction(
                    SearchJourneyAction.JourneyListSearch(
                        JourneyPost(
                            cityFrom = 0,
                            cityTo = 1,
                            dateFrom = "2022-11-24T07:34:07.300Z",
                            dateTo = "2022-11-24T07:34:07.300Z",
                            childrenAmount = 0,
                            adultAmount = 1,
                            disabledAmount = 0
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun CitiesLayout(
    fromCityValue: String,
    toCityValue: String,
    fromCitiesClick: () -> Unit,
    toCitiesClick: () -> Unit,
    swapButtonOnClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        border = BorderStroke(1.dp, GrayBorder),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        ConstraintLayout {
            val (fromCityEt, toCityEt, swapButton, divider) = createRefs()

            CitiesTextField(
                value = fromCityValue,
                labelId = R.string.cities_layout_from_label,
                onClick = fromCitiesClick,
                modifier = Modifier.constrainAs(fromCityEt) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(divider.top)
                    width = Dimension.fillToConstraints
                })

            Divider(
                color = GrayBorder,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(divider) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            CitiesTextField(
                value = toCityValue,
                labelId = R.string.cities_layout_to_label,
                onClick = toCitiesClick,
                modifier = Modifier.constrainAs(toCityEt) {
                    top.linkTo(divider.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                })

            SwapCitiesButton(
                onClick = swapButtonOnClick,
                modifier = Modifier.constrainAs(swapButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, 16.dp)
                })
        }
    }
}

@Composable
private fun CitiesTextField(
    value: String,
    @StringRes labelId: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = {},
        enabled = false,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Gray,
            disabledTextColor = Color.Transparent,
            backgroundColor = White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp, fontWeight = FontWeight.W500, color = Color.Black),
        placeholder = { Text(text = stringResource(id = R.string.cities_layout_hint)) },
        label = { Text(text = stringResource(id = labelId)) },
        modifier = modifier
            .clickable { onClick() }
            .height(62.dp)
    )
}

@Composable
private fun SwapCitiesButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = GrayBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(52.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.swap_cities),
            contentDescription = "swapCities",
            modifier = Modifier.fillMaxSize()
        )
    }
}
