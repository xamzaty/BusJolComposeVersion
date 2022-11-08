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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.launch
import kz.busjol.Language
import kz.busjol.R
import kz.busjol.presentation.NavGraphs
import kz.busjol.presentation.destinations.DirectionDestination
import kz.busjol.presentation.destinations.SearchJourneyScreenDestination
import kz.busjol.presentation.profile.ProfileEvent
import kz.busjol.presentation.profile.ProfileScreenViewModel
import kz.busjol.presentation.startAppDestination
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun ChangeLanguageScreen(
    onCloseBottomSheet: () -> Unit,
    navigator: DestinationsNavigator,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scope = rememberCoroutineScope()

    val isRussianSelected = remember { mutableStateOf(
        state.language == Language.RUSSIAN
    )}

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

        LanguageLayout(
            isLanguageSelected = !isRussianSelected.value,
            isRussian = false
        ) {
            scope.launch {
                isRussianSelected.value = false
                viewModel.onEvent(ProfileEvent.SetLanguage(Language.KAZAKH))
                navigator.backToMainScreen()
            }
        }

        LanguageLayout(
            isLanguageSelected = isRussianSelected.value,
            isRussian = true
        ) {
            scope.launch {
                isRussianSelected.value = true
                viewModel.onEvent(ProfileEvent.SetLanguage(Language.RUSSIAN))
                navigator.backToMainScreen()
            }
        }
    }
}

@Composable
private fun LanguageLayout(
    isLanguageSelected: Boolean,
    isRussian: Boolean,
    onClick: () -> Unit
) {
    val textValue = remember {
        if (isRussian) R.string.russian_language else R.string.kazakh_language
    }

    val fontWeight = remember {
        if (isLanguageSelected) FontWeight.W600 else FontWeight.W400
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

            if (isLanguageSelected) {
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

private fun DestinationsNavigator.backToMainScreen() =
    this.navigate(NavGraphs.root.startAppDestination as DirectionDestination) {
        popUpTo(SearchJourneyScreenDestination) {
            inclusive = true
        }
    }
