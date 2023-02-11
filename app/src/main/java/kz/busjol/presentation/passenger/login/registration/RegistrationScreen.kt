package kz.busjol.presentation.passenger.login.registration

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.data.remote.RegisterPost
import kz.busjol.presentation.*
import kz.busjol.presentation.theme.Blue500
import kz.busjol.utils.Regex.isValidEmail
import kz.busjol.utils.showSnackBar

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun RegistrationScreen(
    navigator: DestinationsNavigator,
    viewModel: RegistrationScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    val loginTextValue = remember { mutableStateOf("") }
    val phoneTextValue = remember { mutableStateOf("") }
    val passwordTextValue = remember { mutableStateOf("") }
    val repeatPasswordTextValue = remember { mutableStateOf("") }

    var checkboxState by rememberSaveable { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(state.isRegistrationSuccess) {
        if (state.isRegistrationSuccess) {
            scaffoldState.showSnackBar(this, "Вы успешно зарегистрированы")
        }
    }

    LaunchedEffect(state.error) {
        if (state.error != null) {
            scaffoldState.showSnackBar(this, state.error)
        }
    }
    
    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .padding(padding)
                .verticalScroll(scrollState)
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
                text = loginTextValue.value,
                onValueChange = {
                    loginTextValue.value = it
                },
                hintId = R.string.email_hint,
                labelId = R.string.email_label,
                modifier = Modifier
                    .padding(start = 15.dp, top = 16.dp, end = 15.dp),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )

            MaskTextField(
                text = phoneTextValue.value,
                onValueChange = {
                    phoneTextValue.value = it
                },
                hintId = R.string.phone_hint,
                labelId = R.string.phone_label,
                modifier = Modifier
                    .padding(start = 15.dp, top = 16.dp, end = 15.dp)
                    .onPreviewKeyEvent {
                        if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN){
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else {
                            false
                        }
                    },
                textFieldMaskVariants = TextFieldMaskVariants.PHONE,
                maxChar = 10,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Phone
                )
            )

            CustomTextField(
                text = passwordTextValue.value,
                onValueChange = {
                    passwordTextValue.value = it
                },
                hintId = R.string.password_hint,
                labelId = R.string.password_label,
                modifier = Modifier
                    .padding(start = 15.dp, top = 16.dp, end = 15.dp)
                    .onPreviewKeyEvent {
                        if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN){
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        } else {
                            false
                        }
                    },
                isHiddenToggleVisible = true,
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                )
            )

            CustomTextField(
                text = repeatPasswordTextValue.value,
                onValueChange = {
                    repeatPasswordTextValue.value = it
                },
                hintId = R.string.password_hint,
                labelId = R.string.repeat_password_hint,
                keyboardActions = KeyboardActions(
                    onDone = {keyboardController?.hide()}),
                modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp),
                isHiddenToggleVisible = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,),
                isError = repeatPasswordTextValue.value.isNotEmpty() &&
                        repeatPasswordTextValue.value != passwordTextValue.value,
                errorText = stringResource(id = R.string.password_mismatch_error),
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
                enabled = true,
                modifier = Modifier.padding(start = 15.dp, top = 25.5.dp, end = 15.dp)
            ) {
                scope.launch {
                    keyboardController?.hide()

                    when {
                        loginTextValue.value.isEmpty() || phoneTextValue.value.isEmpty() ||
                                passwordTextValue.value.isEmpty() -> {
                            scaffoldState.showSnackBar(this, context.getString(R.string.fill_in_all_the_fields_error))
                        }

                        phoneTextValue.value.length < 10 -> {
                            scaffoldState.showSnackBar(this, "Введите номер телефона полностью")
                        }

                        passwordTextValue.value != repeatPasswordTextValue.value -> {
                            scaffoldState.showSnackBar(this, context.getString(R.string.password_mismatch_error))
                        }

                        passwordTextValue.value.length < 8 -> {
                            scaffoldState.showSnackBar(this, "Пароль не может быть меньше 8 символов")
                        }

                        !checkboxState -> {
                            scaffoldState.showSnackBar(this, context.getString(R.string.personal_data_collection_error))
                        }

                        !loginTextValue.value.isValidEmail() -> {
                            scaffoldState.showSnackBar(this, "Электронный адрес не правильный")
                        }

                        else -> {
                            viewModel.onEvent(
                                RegistrationEvent.OnRegisterButtonClicked(
                                    RegisterPost(
                                        email = loginTextValue.value.trim(),
                                        password = passwordTextValue.value.trim(),
                                        phoneNumber = "+7${phoneTextValue.value}".trim()
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