package kz.busjol.presentation.passenger.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.presentation.theme.GrayText

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginScreen(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    EnterScreen(sheetState, coroutineScope)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EnterScreen(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {
        AppBar(title = stringResource(id = R.string.enter_title)) {
            coroutineScope.launch { sheetState.hide() }
        }

        Text(
            text = stringResource(id = R.string.enter_description_title),
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.enter_description_text),
            color = GrayText,
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 8.dp)
        )
    }
}

@Composable
private fun PasswordRecoveryScreen(screenType: LoginScreenType) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBar(title = stringResource(id = R.string.enter_title)) {
        }


    }
}

@Composable
private fun RegistrationScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBar(title = stringResource(id = R.string.enter_title)) {
        }


    }
}

