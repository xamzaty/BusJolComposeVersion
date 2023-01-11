package kz.busjol.presentation.passenger.login.password_recovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import kz.busjol.presentation.ProgressButton

@Destination
@Composable
fun PasswordRecoveryScreen(
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    val emailTextValue = rememberSaveable { mutableStateOf("") }
    val buttonAvailability = rememberSaveable {
        mutableStateOf(emailTextValue.value.isNotEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        AppBar(title = stringResource(id = R.string.password_recovery_title)) {
            scope.launch {
                navigator.navigateUp()
            }
        }

        Text(
            text = stringResource(id = R.string.password_recovery_description_text),
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
            keyboardType = KeyboardType.Email,
            labelId = R.string.email_label,
            modifier = Modifier
                .padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        ProgressButton(
            textId = R.string.send_button,
            enabled = buttonAvailability.value,
            modifier = Modifier.padding(start = 15.dp, top = 24.dp, end = 15.dp)
        ) {

        }
    }
}