package kz.busjol.presentation.profile.change_language

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import kz.busjol.Language
import kz.busjol.R
import kz.busjol.presentation.profile.ProfileEvent
import kz.busjol.presentation.profile.ProfileScreenViewModel
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.utils.backToMainScreen
import kz.busjol.utils.findActivity

@Composable
fun ChangeLanguageScreen(
    onCloseBottomSheet: () -> Unit,
    navigator: DestinationsNavigator,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val selectedLanguage = remember { mutableStateOf(state.language) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = "close",
                modifier = Modifier
                    .size(30.dp)
                    .padding(start = 15.dp)
                    .clickable { onCloseBottomSheet() }
            )

            Text(
                text = stringResource(id = R.string.app_language_title),
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            color = GrayBorder,
            thickness = 1.dp
        )

        Language.values().forEach { language ->
            LanguageLayout(
                language = language,
                isSelected = selectedLanguage.value == language
            ) {
                scope.launch {
                    selectedLanguage.value = language
                    viewModel.onEvent(ProfileEvent.SetLanguage(language))
                    navigator.backToMainScreen()
                    context.findActivity()?.recreate()
                }
            }
        }
    }
}

@Composable
private fun LanguageLayout(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textValue = remember {
        when (language) {
            Language.RUSSIAN -> R.string.russian_language
            Language.KAZAKH -> R.string.kazakh_language
        }
    }

    val fontWeight = remember {
        if (isSelected) FontWeight.W600 else FontWeight.W400
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(id = textValue),
                fontSize = 16.sp,
                color = Color(0xFF1A1C1F),
                fontWeight = fontWeight
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.isselected),
                    contentDescription = "isSelected"
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = GrayBorder,
            thickness = 1.dp
        )
    }
}
