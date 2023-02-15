package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.data.remote.BookingElements
import kz.busjol.data.remote.BookingPost
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.CustomTextFieldWithMask
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.destinations.BookingScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.journey_details.JourneyDetailsScreen
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.passenger.buy_ticket.search_journey.passenger_quantity.Passenger
import kz.busjol.presentation.theme.*
import kz.busjol.utils.MaskVisualTransformation
import kz.busjol.utils.Regex.isValidEmail
import kz.busjol.utils.showSnackBar
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun PassengerDataScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent =
        {
            JourneyDetailsScreen(sheetState, coroutineScope, ticket)
        })
    {
        Scaffold(scaffoldState = scaffoldState) {
            MainContent(
                ticket = ticket,
                paddingValues = it,
                scaffoldState = scaffoldState,
                navigator = navigator,
                sheetState = sheetState,
                coroutineScope = coroutineScope
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun MainContent(
    paddingValues: PaddingValues,
    scaffoldState: ScaffoldState,
    ticket: Ticket,
    navigator: DestinationsNavigator,
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    viewModel: PassengerViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val checkboxState = remember { mutableStateOf(true) }
    val emailValue = remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }

    val formErrorText = remember { mutableStateOf("") }

    val bookingElementList = rememberSaveable {
        mutableListOf<BookingElements>()
    }
    val addBookingElement = remember {
        mutableListOf<BookingElements>()
    }
    val isSetDataToList = rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val currentDate: String = sdf.format(Date())

    val phoneMask = "+7 ### ### ## ##"

    DisposableEffect(state.setDataToList) {
        onDispose {
            viewModel.onEvent(PassengerDataEvent.SetDataToListStatusFalse(false))
        }
    }

    if (state.startNewDestination) {
        LaunchedEffect(navigator) {
            navigator.navigate(
                BookingScreenDestination(
                    ticket = Ticket(
                        departureCity = ticket.departureCity,
                        arrivalCity = ticket.arrivalCity,
                        date = ticket.date,
                        passengerList = ticket.passengerList,
                        journey = ticket.journey,
                        bookingList = state.booking
                    )
                )
            )
        }
    }

    if (state.error?.isNotEmpty() == true) {
        LaunchedEffect(scaffoldState.snackbarHostState) {
            scaffoldState.showSnackBar(this, state.error)
        }
    }

    if (isSetDataToList.value) {
        LaunchedEffect(isSetDataToList) {

        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(GrayBackground),
        content = {
            stickyHeader {
                AppBar(
                    title = stringResource(
                        id = R.string.passenger_data_title
                    )
                ) {
                    coroutineScope.launch {
                        navigator.navigateUp()
                    }
                }
            }

            item {
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
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.journey_number, "23"),
                            fontSize = 11.sp,
                            color = Color.Black
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = "${ticket.departureCity?.name} - ${ticket.arrivalCity?.name}",
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
                                    coroutineScope.launch {
                                        sheetState.show()
                                    }
                                }
                            )
                        }

                        Text(
                            text = ticket.journey?.departureTime ?: "",
                            color = GrayText,
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }

            val list = ticket.passengerList ?: listOf(Passenger())

            itemsIndexed(list) { index, item ->
                PassengerRegistrationLayout(
                    count = index + 1,
                    passenger = item,
                    seatId = item.seatId ?: 1,
                    focusManager = focusManager,
                    errorText = {
                        formErrorText.value = it
                    },
                    bookingElements = {
                        coroutineScope.launch {
                            bookingElementList.addAll(it)
                        }
                    }
                )
            }

            item {
                if (!state.isPassengerHaveLogin) {
                    Card(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GrayBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 15.dp, end = 15.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {

                            Text(
                                text = stringResource(id = R.string.contact_information_title),
                                fontWeight = FontWeight.W700,
                                color = Color.Black,
                                fontSize = 16.sp
                            )

                            Text(
                                text = stringResource(id = R.string.contact_information_description),
                                fontWeight = FontWeight.W500,
                                fontSize = 12.sp,
                                color = Color(0xFF8B98A7),
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            CustomTextField(
                                text = emailValue.value,
                                onValueChange = {
                                    emailValue.value = it
                                },
                                iconId = null,
                                hintId = R.string.email_hint,
                                labelId = R.string.email_label,
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                ),
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Email
                                ),
                                modifier = Modifier.padding(top = 12.dp)
                            )

                            CustomTextFieldWithMask(
                                text = phoneValue.value,
                                onValueChange = {
                                    phoneValue.value = it
                                },
                                iconId = null,
                                hintId = R.string.email_hint,
                                labelId = R.string.phone_label,
                                maxChar = 10,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next,
                                    keyboardType = KeyboardType.Phone
                                ),
                                modifier = Modifier.padding(top = 12.dp),
                                maskVisualTransformation = MaskVisualTransformation(phoneMask)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GrayBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 15.dp, end = 15.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {

                        Text(
                            text = stringResource(id = R.string.tariff_rules_and_offer_title),
                            fontWeight = FontWeight.W700,
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Text(
                            text = stringResource(id = R.string.tariff_rules_and_offer_description),
                            fontWeight = FontWeight.W500,
                            fontSize = 12.sp,
                            color = Color(0xFF8B98A7),
                            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .fillMaxWidth()
                        ) {

                            Checkbox(
                                checked = checkboxState.value,
                                onCheckedChange = {
                                    checkboxState.value = it
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Blue500
                                )
                            )

                            Text(
                                text = stringResource(id = R.string.tariff_rules_and_offer_agreement),
                                fontSize = 12.sp,
                                color = Color(0xFF959593),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        if (!checkboxState.value) {
                            Text(
                                text = stringResource(id = R.string.this_field_is_required),
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp, start = 16.dp)
                            )
                        }
                    }
                }
            }

            item {
                Card(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 29.dp)
                        .offset(y = 8.dp)
                ) {
                    ProgressButton(
                        textId = R.string.continue_button,
                        isProgressBarActive = state.isLoading,
                        enabled = true,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 15.dp)
                    ) {
                        coroutineScope.launch {
                            when {
                                formErrorText.value.isNotEmpty() -> {
                                    scaffoldState.showSnackBar(this, formErrorText.value)
                                }

                                !state.isPassengerHaveLogin && (emailValue.value.isEmpty() ||
                                        phoneValue.value.isEmpty()) -> {
                                    scaffoldState.showSnackBar(this, "Заполните все поля")
                                }

                                !state.isPassengerHaveLogin && !emailValue.value.isValidEmail() -> {
                                    scaffoldState.showSnackBar(
                                        this,
                                        "Неправильный электронный адрес"
                                    )
                                }

                                !state.isPassengerHaveLogin && phoneValue.value.length < 10 -> {
                                    scaffoldState.showSnackBar(
                                        this,
                                        "Введите номер телефона полностью"
                                    )
                                }

                                !checkboxState.value -> {
                                    scaffoldState.showSnackBar(
                                        this,
                                        "Чтобы продолжить, необходимо согласиться с правилами оферты и возврата"
                                    )
                                }

                                else -> {
                                    keyboardController?.hide()
                                    isSetDataToList.value = true
                                    addBookingElement.addAll(bookingElementList)

                                    viewModel.onEvent(
                                        PassengerDataEvent.OnContinueButtonAction(
                                            BookingPost(
                                                bookingElements = bookingElementList,
                                                email = emailValue.value.trim(),
                                                phoneNumber = "+7${phoneValue.value.trim()}",
                                                phoneTimeAtCreating = currentDate.trim(),
                                                clientId = 0,
                                                segmentId = ticket.journey?.segmentId ?: 0
                                            )
                                        )
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun PassengerRegistrationLayout(
    count: Int,
    passenger: Passenger,
    seatId: Int,
    focusManager: FocusManager,
    errorText: (String) -> Unit,
    bookingElements: (List<BookingElements>) -> Unit,
) {
    val passengerSex = rememberSaveable {
        mutableStateOf(0)
    }

    val iinValue = rememberSaveable {
        mutableStateOf("")
    }

    val lastNameValue = rememberSaveable {
        mutableStateOf("")
    }

    val firstNameValue = rememberSaveable {
        mutableStateOf("")
    }

    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GrayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 15.dp, end = 15.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.passenger_number, count),
                    fontWeight = FontWeight.W500,
                    fontSize = 17.sp
                )

                Text(
                    text = when (passenger.type) {
                        Passenger.PassengerType.ADULT -> stringResource(id = R.string.adult_passenger)
                        Passenger.PassengerType.CHILD -> stringResource(id = R.string.child_passenger)
                        Passenger.PassengerType.DISABLED -> stringResource(id = R.string.disabled_person)
                        else -> ""
                    },
                    color = GrayText,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            RadioGroup(
                listOfOptions = listOf(
                    stringResource(id = R.string.passenger_type_men),
                    stringResource(id = R.string.passenger_type_women)
                ),
                selectedOptionValue = passengerSex.value,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                passengerSex.value = it
            }

            CustomTextFieldWithMask(
                text = iinValue.value,
                onValueChange = {
                    iinValue.value = it
                },
                hintId = R.string.iin_hint,
                labelId = R.string.iin_label,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                maxChar = 12,
                modifier = Modifier.padding(top = 12.dp),
                maskVisualTransformation = MaskVisualTransformation("### ### ### ###")
            )

            CustomTextField(
                text = lastNameValue.value,
                onValueChange = {
                    lastNameValue.value = it
                },
                hintId = R.string.surname_hint,
                labelId = R.string.surname_label,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.padding(top = 12.dp)
            )

            CustomTextField(
                text = firstNameValue.value,
                onValueChange = {
                    firstNameValue.value = it
                },
                hintId = R.string.first_name_hint,
                labelId = R.string.first_name_label,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier.padding(top = 12.dp)
            )

            when {
                iinValue.value.isEmpty() || firstNameValue.value.isEmpty() ||
                        lastNameValue.value.isEmpty() || iinValue.value.length < 12 -> {
                    errorText("Заполните все поля в анкете")
                }
                else -> errorText("")
            }

            val arrayList = arrayListOf<BookingElements>()

            val bookingElement = BookingElements(
                iin = iinValue.value,
                firstName = firstNameValue.value.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                    else it.toString()
                },
                lastName = lastNameValue.value.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                    else it.toString()
                },
                sex = passengerSex.value,
                seatId = seatId
            )

            arrayList.add(bookingElement)

            bookingElements(arrayList)
        }
    }
}

@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    listOfOptions: List<String>,
    selectedOptionValue: Int,
    selectedValue: (Int) -> Unit
) {
    val selectedOption = remember {
        mutableStateOf(selectedOptionValue)
    }

    val onSelectionChange = { position: Int ->
        selectedOption.value = position
    }

    LazyRow(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(listOfOptions) { index, text ->
            Card(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, GrayBorder),
                elevation = 0.dp,
                modifier = Modifier
                    .width(147.dp)
                    .height(40.dp)
                    .clickable { onSelectionChange(index) }
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.body1.merge(),
                    textAlign = TextAlign.Center,
                    color = if (index == selectedOption.value) Color.White else Color.Black,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (index == selectedOption.value) Blue500 else Color.White)
                        .wrapContentHeight()
                )
            }
        }
    }
    selectedValue(selectedOption.value)
}