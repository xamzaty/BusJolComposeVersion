package kz.busjol.presentation.passenger.passenger_data

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.passenger.journey_details.JourneyDetailsScreen
import kz.busjol.presentation.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PassengerDataScreen() {
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

    var checkboxState by rememberSaveable { mutableStateOf(true) }
    val emailValue by rememberSaveable { mutableStateOf("") }
    val phoneValue by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {

        AppBar(title = stringResource(
            id = R.string.passenger_data_title)
        ) {

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
                    modifier = Modifier.padding(top = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W500
                )
            }
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
                    modifier = Modifier.padding(top = 12.dp)
                )

                CustomTextField(
                    text = phoneValue,
                    iconId = null,
                    hintId = R.string.email_hint,
                    labelId = R.string.phone_label,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
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

        Spacer(modifier = Modifier
            .padding(top = 28.dp)
            .weight(1f)
        )


    }
}

@Composable
private fun PassengerRegistrationLayout(count: Int) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, GrayBorder),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 15.dp, end = 15.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, )
        ) {

        }
    }
}
