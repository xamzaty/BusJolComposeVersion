package kz.busjol.presentation.driver.passenger_verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.theme.GreenTicketIsValid
import kz.busjol.presentation.theme.RedTicketIsInvalid

@Destination
@Composable
fun PassengerVerificationScreen() {

    val isTicketValid = remember { true }

    val mainFrameBackgroundColor = remember {
        if (isTicketValid) GreenTicketIsValid else RedTicketIsInvalid
    }

    val mainTitle = remember {
        if (isTicketValid) R.string.ticket_is_valid else R.string.ticket_is_invalid
    }

    val mainImage = remember {
        if (isTicketValid) R.drawable.ticket_valid else R.drawable.ticket_invalid
    }

    val iinValue = remember { "960 717 300 890" }
    val nameValue = remember { "Ержанов Хамзат Ержанулы" }
    val routeValue = remember { "Алматы - Караганда" }

    Column(
        Modifier.fillMaxSize()
    ) {
        AppBar(title = stringResource(id = R.string.scan_title)) {
            
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(mainFrameBackgroundColor)
        ) {

            Column(
                modifier = Modifier.padding(vertical = 26.5.dp, horizontal = 15.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.journey_number, "23"),
                    fontSize = 14.sp,
                    color = Color.White
                )

                Text(
                    text = stringResource(mainTitle),
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W700,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(mainImage),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 15.dp)
                )
        }
        
        PassengerDataLayout(
            title = stringResource(id = R.string.iin_label),
            value = iinValue,
            modifier = Modifier.padding(
                start = 15.dp, top = 24.dp, end = 15.dp
            )
        )

        PassengerDataLayout(
            title = stringResource(id = R.string.fio),
            value = nameValue,
            modifier = Modifier.padding(
                start = 15.dp, top = 12.dp, end = 15.dp
            )
        )

        PassengerDataLayout(
            title = stringResource(id = R.string.route),
            value = routeValue,
            modifier = Modifier.padding(
                start = 15.dp, top = 12.dp, end = 15.dp
            )
        )
    }
}

@Composable
private fun PassengerDataLayout(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.W600
        )

        Text(
            text = value,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 9.dp)
        )
    }
}
