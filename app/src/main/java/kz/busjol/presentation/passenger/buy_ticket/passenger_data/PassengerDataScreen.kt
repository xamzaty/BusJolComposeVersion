package kz.busjol.presentation.passenger.buy_ticket.passenger_data

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.CustomTextFieldWithMask
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.destinations.BookingScreenDestination
import kz.busjol.presentation.passenger.buy_ticket.journey_details.JourneyDetailsScreen
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.*
import kz.busjol.utils.MaskVisualTransformation
import kz.busjol.utils.Regex.isValidEmail

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
        MainContent(ticket, navigator, sheetState, coroutineScope)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun MainContent(
    ticket: Ticket,
    navigator: DestinationsNavigator,
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {

    val context = LocalContext.current

    var checkboxState by rememberSaveable { mutableStateOf(true) }
    val emailValue by rememberSaveable { mutableStateOf("") }
    val phoneValue by rememberSaveable { mutableStateOf("") }

    var isEmailValid = remember { isValidEmail(emailValue) }

    val phoneMask = "+7 ### ### ## ##"

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        content = {
            stickyHeader {
                AppBar(
                    title = stringResource(
                        id = R.string.passenger_data_title
                    )
                ) {
                    navigator.navigateUp()
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
                                    coroutineScope.launch { sheetState.show() }
                                }
                            )
                        }

                        Text(
                            text = "24.12.2022 09:00",
                            color = GrayText,
                            modifier = Modifier.padding(top = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }

            itemsIndexed(ticket.passengerList ?: emptyList()) { index, _ ->
                PassengerRegistrationLayout(count = index)
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
                            text = emailValue,
                            iconId = null,
                            hintId = R.string.email_hint,
                            labelId = R.string.email_label,
                            keyboardType = KeyboardType.Email,
                            modifier = Modifier.padding(top = 12.dp)
                        )

                        CustomTextFieldWithMask(
                            text = phoneValue,
                            iconId = null,
                            hintId = R.string.email_hint,
                            labelId = R.string.phone_label,
                            keyboardType = KeyboardType.Phone,
                            maxChar = 10,
                            modifier = Modifier.padding(top = 12.dp),
                            maskVisualTransformation = MaskVisualTransformation(phoneMask)
                        )
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
                                checked = checkboxState,
                                onCheckedChange = {
                                    checkboxState = it
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

                        if (!checkboxState) {
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
                        .offset(y = 6.dp)
                ) {
                    ProgressButton(
                        textId = R.string.continue_button,
                        isProgressAvailable = false,
                        isEnabled = true,
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 15.dp)
                    ) {
                        navigator.navigate(
                            BookingScreenDestination(
                                ticket = Ticket(
                                    departureCity = ticket.departureCity,
                                    arrivalCity = ticket.arrivalCity,
                                    date = ticket.date,
                                    passengerList = ticket.passengerList,
                                    journey = ticket.journey
                                )
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun PassengerRegistrationLayout(
    count: Int,
    viewModel: PassengerViewModel = hiltViewModel()
) {
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
                    text = "Взрослый",
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
                selectedOptionValue = 0,
                modifier = Modifier.padding(top = 12.dp)
            )

            CustomTextFieldWithMask(
                text = "",
                hintId = R.string.iin_hint,
                labelId = R.string.iin_label,
                keyboardType = KeyboardType.Number,
                maxChar = 12,
                modifier = Modifier.padding(top = 12.dp),
                maskVisualTransformation = MaskVisualTransformation("### ### ### ###")
            )

            CustomTextField(
                text = "",
                hintId = R.string.surname_hint,
                labelId = R.string.surname_label,
                modifier = Modifier.padding(top = 12.dp)
            )

            CustomTextField(
                text = "",
                hintId = R.string.first_name_hint,
                labelId = R.string.first_name_label,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    listOfOptions: List<String>,
    selectedOptionValue: Int
) {
    var selectedOption by rememberSaveable {
        mutableStateOf(selectedOptionValue)
    }

    val onSelectionChange = { position: Int ->
        selectedOption = position
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
                    color = if (index == selectedOption) Color.White else Color.Black,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (index == selectedOption) Blue500 else Color.White)
                        .wrapContentHeight()
                )
            }
        }
    }
}
