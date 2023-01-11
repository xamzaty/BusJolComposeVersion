package kz.busjol.presentation.passenger.login.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.HtmlText
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.theme.Blue500

@Destination
@Composable
fun RegistrationScreen(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    val emailTextValue = rememberSaveable { mutableStateOf("") }
    var checkboxState by rememberSaveable { mutableStateOf(true) }

    val buttonAvailability by rememberSaveable {
        mutableStateOf(emailTextValue.value.isNotEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        AppBar(title = stringResource(id = R.string.register_title)) {
            scope.launch {
                navigator.navigateUp()
            }
        }

        Text(
            text = stringResource(id = R.string.register_description_text),
            color = Color(0xFF8B98A7),
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        CustomTextField(
            text = emailTextValue.value,
            onValueChange = {
                emailTextValue.value = it
            },
            hintId = R.string.email_hint,
            labelId = R.string.email_label,
            keyboardType = KeyboardType.Email,
            modifier = Modifier
                .padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        if (!checkboxState) {
            Text(
                text = stringResource(id = R.string.personal_data_collection_error),
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 15.dp, top = 2.dp, end = 15.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 25.5.dp, end = 15.dp)
        ) {
            HtmlText(
                textId = R.string.personal_data_collection_consent,
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = checkboxState,
                onCheckedChange = {
                    checkboxState = it
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Blue500,
                    checkedThumbColor = Color.White
                )
            )
        }

        ProgressButton(
            textId = R.string.register_button,
            enabled = buttonAvailability,
            modifier = Modifier.padding(start = 15.dp, top = 25.5.dp, end = 15.dp)
        ) {

        }
    }
}