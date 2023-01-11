package kz.busjol.presentation.passenger.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.ProgressButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.BuildConfig
import kz.busjol.UserState
import kz.busjol.presentation.destinations.PasswordRecoveryScreenDestination
import kz.busjol.presentation.destinations.RegistrationScreenDestination
import kz.busjol.presentation.theme.Blue500

@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    val emailTextValue = remember { mutableStateOf("") }
    val passwordTextValue = remember { mutableStateOf("") }

    val buttonAvailability = remember {
        emailTextValue.value.isNotEmpty()
    }

    println(buttonAvailability)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        AppBar(title = stringResource(id = R.string.enter_title), isCross = true) {
            scope.launch {
                navigator.navigateUp()
            }
        }

        Text(
            text = stringResource(id = R.string.enter_description_title),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        Text(
            text = stringResource(id = R.string.enter_description_text),
            color = Color(0xFF8B98A7),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 15.dp, top = 8.dp, end = 15.dp)
        )

        CustomTextField(
            text = emailTextValue.value,
            onValueChange = {
                emailTextValue.value = it
                viewModel.onEvent(LoginEvent.GetEmailValue(it))
            },
            keyboardType = KeyboardType.Email,
            hintId = R.string.email_hint,
            labelId = R.string.email_label,
            modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        CustomTextField(
            text = passwordTextValue.value,
            onValueChange = {
                passwordTextValue.value = it
                viewModel.onEvent(LoginEvent.GetPasswordValue(it))
            },
            hintId = R.string.password_hint,
            labelId = R.string.password_label,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.padding(start = 15.dp, top = 8.dp, end = 15.dp)
        )

        ProgressButton(
            textId = R.string.enter_button,
            enabled = buttonAvailability,
            modifier = Modifier.padding(start = 15.dp, top = 24.dp, end = 15.dp)
        ) {
            if (
                emailTextValue.value == "driver@gmail.com" &&
                passwordTextValue.value == "12345"
            ) {
                viewModel.onEvent(LoginEvent.SetUserState(UserState.DRIVER))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 24.dp, end = 15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = Blue500,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable { 
                        scope.launch {
                            navigator.navigate(
                                PasswordRecoveryScreenDestination()
                            )
                        }
                    }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = stringResource(id = R.string.register_button),
                color = Blue500,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable {
                        scope.launch {
                            navigator.navigate(
                                RegistrationScreenDestination()
                            )
                        }
                    }
            )
        }
    }
}
