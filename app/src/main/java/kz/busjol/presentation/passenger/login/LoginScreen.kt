package kz.busjol.presentation.passenger.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import kz.busjol.data.remote.AuthenticatePost
import kz.busjol.presentation.CustomTextFieldWithMask
import kz.busjol.presentation.destinations.PasswordRecoveryScreenDestination
import kz.busjol.presentation.destinations.RegistrationScreenDestination
import kz.busjol.presentation.theme.Blue500
import kz.busjol.utils.MaskVisualTransformation
import kz.busjol.utils.Regex.containsLetters
import kz.busjol.utils.Regex.isValidEmail
import kz.busjol.utils.backToMainScreen
import kz.busjol.utils.showSnackBar

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val loginTextValue = remember { mutableStateOf("") }
    val passwordTextValue = remember { mutableStateOf("") }

    val phoneMask = "## ### ### ## ##"
    val phoneMaskWithEight = "# ### ### ## ##"
    val emailMask = "######################################################"

    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(scaffoldState = scaffoldState) { padding ->

        LaunchedEffect(state.error) {
            if (state.error != null) {
                scaffoldState.showSnackBar(this, state.error)
            }
        }

        LaunchedEffect(state.returnToMainScreen) {
            if (state.returnToMainScreen) {
                navigator.backToMainScreen()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(padding)
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

            CustomTextFieldWithMask(
                text = loginTextValue.value,
                onValueChange = {
                    loginTextValue.value = it
                },
                keyboardType =
                if (loginTextValue.value.startsWith("+7")) KeyboardType.Phone
                else KeyboardType.Email,
                hintId = R.string.email_hint,
                labelId = R.string.email_or_phone_label,
                modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp),
                maxChar = when {
                    loginTextValue.value.startsWith("+7") -> 12
                    loginTextValue.value.startsWith("87") -> 11
                    else -> 100
                },
                maskVisualTransformation = MaskVisualTransformation(
                    when {
                        loginTextValue.value.startsWith("+7") -> phoneMask
                        loginTextValue.value.startsWith("87") -> phoneMaskWithEight
                        else -> emailMask
                    }
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            CustomTextField(
                text = passwordTextValue.value,
                onValueChange = {
                    passwordTextValue.value = it
                },
                hintId = R.string.password_hint,
                labelId = R.string.password_label,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(start = 15.dp, top = 8.dp, end = 15.dp),
                isHiddenToggleVisible = true
            )

            ProgressButton(
                textId = R.string.enter_button,
                enabled = true,
                isProgressBarActive = state.isLoading,
                modifier = Modifier.padding(start = 15.dp, top = 24.dp, end = 15.dp)
            ) {
                scope.launch {
                    keyboardController?.hide()

                    when {
                        loginTextValue.value.isEmpty() || passwordTextValue.value.isEmpty() -> {
                            scaffoldState.showSnackBar(this, "Заполните все поля!")
                        }

                        loginTextValue.value.containsLetters() && !loginTextValue.value.isValidEmail() -> {
                            scaffoldState.showSnackBar(this, "Неправильный электронный адрес")
                        }

                        else -> {
                            viewModel.onEvent(
                                LoginEvent.OnLoginButtonPressed(
                                    AuthenticatePost(
                                        loginInfo = when {
                                            loginTextValue.value.startsWith("87") -> {
                                                loginTextValue.value
                                                    .replaceFirst("8", "+7").trim()
                                            }
                                            else ->  {
                                                loginTextValue.value.trim()
                                            }},
                                        password = passwordTextValue.value.trim()
                                    )
                                )
                            )
                        }
                    }
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
}