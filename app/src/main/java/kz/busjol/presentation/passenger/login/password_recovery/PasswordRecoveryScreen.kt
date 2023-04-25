package kz.busjol.presentation.passenger.login.password_recovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.data.remote.RestorePasswordPost
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.ProgressButton
import kz.busjol.utils.showSnackBar

@OptIn(ExperimentalComposeUiApi::class)
@Destination
@Composable
fun PasswordRecoveryScreen(
    navigator: DestinationsNavigator,
    viewModel: PasswordRecoveryViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val emailTextValue = remember { mutableStateOf("") }
    val passwordTextValue = remember { mutableStateOf("") }
    val secretCodeTextValue = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current

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
                labelId = R.string.email_label,
                modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                )
            )

            CustomTextField(
                text = passwordTextValue.value,
                onValueChange = {
                    passwordTextValue.value = it
                },
                hintId = R.string.password_hint,
                visualTransformation = PasswordVisualTransformation(),
                labelId = R.string.password_label,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isHiddenToggleVisible = true,
                modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp)
            )

            ProgressButton(
                textId = R.string.send_button,
                enabled = true,
                progressBarActiveState = state.isLoading,
                modifier = Modifier.padding(start = 15.dp, top = 24.dp, end = 15.dp)
            ) {
                scope.launch {
                    keyboardController?.hide()

                    if (emailTextValue.value.isEmpty()) {
                        scaffoldState.showSnackBar(this, "Заполните поля")
                    } else {
                        viewModel.onEvent(
                            PasswordRecoveryEvent.OnSendButtonClicked(
                                RestorePasswordPost(
                                    password = passwordTextValue.value,
                                    secretCode = secretCodeTextValue.value,
                                    loginInfo = emailTextValue.value
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}