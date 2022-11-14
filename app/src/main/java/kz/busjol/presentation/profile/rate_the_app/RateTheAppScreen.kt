package kz.busjol.presentation.profile.rate_the_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import kz.busjol.presentation.theme.GrayBorder

@Destination
@Composable
fun RateTheApp(onCloseBottomSheet: () -> Unit) {
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
                text = stringResource(id = R.string.rate_the_app_title),
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

        Image(
            painter = painterResource(id = R.drawable.rate_the_app),
            contentDescription = "rate_the_app",
            modifier = Modifier
                .size(150.dp)
                .padding(top = 24.dp)
        )

        RateButton(
            isLikeApp = true,
            modifier = Modifier.padding(start = 15.dp, top = 24.dp, end = 15.dp)
        ) {
            
        }

        RateButton(
            isLikeApp = false,
            modifier = Modifier.padding(start = 15.dp, top = 12.dp, end = 15.dp, bottom = 24.dp)
        ) {

        }
    }
}

@Composable
private fun RateButton(
    modifier: Modifier = Modifier,
    isLikeApp: Boolean,
    onClick: () -> Unit
) {

    val text = remember { if (isLikeApp) R.string.i_like_the_app else R.string.i_do_not_like_the_app }
    val image = remember { if (isLikeApp) R.drawable.like else R.drawable.dislike }

    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF2F6F8)),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = ""
        )

        Text(
            text = stringResource(id = text),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF2898FF),
            modifier = Modifier.padding(start = 4.83.dp)
        )
    }
}
