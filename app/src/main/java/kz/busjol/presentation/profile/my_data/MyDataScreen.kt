package kz.busjol.presentation.profile.my_data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.CustomTextField
import kz.busjol.presentation.ProgressButton

@Destination
@Composable
fun MyDataScreen() {

    val surnameValue = remember { "" }
    val firstNameValue = remember { "" }
    val birthDateValue = remember { "" }
    val phoneNumberValue = remember { "" }

    val verticalScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
    ) {
        AppBar(title = stringResource(id = R.string.my_data_subtitle)) {
            
        }

        CustomTextField(
            text = surnameValue,
            onValueChange = {

            },
            hintId = R.string.surname_hint,
            labelId = R.string.surname_label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 16.dp, end = 15.dp)
        )

        CustomTextField(
            text = firstNameValue,
            onValueChange = {

            },
            hintId = R.string.first_name_hint,
            labelId = R.string.first_name_label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, end = 15.dp)
        )

        CustomTextField(
            text = birthDateValue,
            onValueChange = {

            },
            hintId = R.string.date_of_birth_hint,
            labelId = R.string.date_of_birth_label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, end = 15.dp)
        )

        CustomTextField(
            text = phoneNumberValue,
            onValueChange = {

            },
            hintId = R.string.phone_hint,
            labelId = R.string.phone_label,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 8.dp, end = 15.dp)
        )

        ProgressButton(
            textId = R.string.continue_button,
            isProgressBarActive = false,
            enabled = true,
            modifier = Modifier
                .padding(start = 15.dp, top = 24.dp, end = 15.dp)
        ) {

        }
    }
}