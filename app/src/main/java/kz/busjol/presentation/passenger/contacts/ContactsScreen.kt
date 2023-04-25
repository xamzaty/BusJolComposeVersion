package kz.busjol.presentation.passenger.contacts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import kz.busjol.R
import kz.busjol.presentation.CustomOutlinedButton
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.theme.GrayText

@Destination
@Composable
fun ContactsScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 22.dp, start = 15.dp, end = 15.dp, bottom = 16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.contacts_title),
            fontWeight = FontWeight.W700,
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Image(
            painter = painterResource(id = R.drawable.contacts_image),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 86.dp)
                .size(128.dp)
        )

        Text(
            text = stringResource(id = R.string.contacts_description_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(id = R.string.contacts_description_text),
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .width(200.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomOutlinedButton(textId = R.string.faq_button) {

        }

        ProgressButton(
            textId = R.string.call_button,
            progressBarActiveState = false,
            enabled = true,
            modifier = Modifier.padding(top = 16.dp)
        ) {

        }
    }
}