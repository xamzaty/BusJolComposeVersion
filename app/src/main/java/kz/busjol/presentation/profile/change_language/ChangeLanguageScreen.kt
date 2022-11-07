package kz.busjol.presentation.profile.change_language

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.busjol.R
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun ChangeLanguageScreen(onCloseBottomSheet: () -> Unit) {

    val isRussianSelected = remember { mutableStateOf(true) }

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
            isRussianSelected.value = false
        }

        LanguageLayout(
            isLanguageSelected = isRussianSelected.value,
            isRussian = true
        ) {
            isRussianSelected.value = true
        }
    }
}

@Composable
private fun LanguageLayout(
    isLanguageSelected: Boolean,
    isRussian: Boolean,
    onClick: () -> Unit
) {
    val textValue = remember { if (isRussian) R.string.russian_language else R.string.kazakh_language }

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
                color = Color(0xFF1A1C1F)
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
