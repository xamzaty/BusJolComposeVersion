package kz.busjol.presentation.passenger.choose_seats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.theme.Blue500
import kz.busjol.R
import kz.busjol.presentation.theme.DarkGrayBackground
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.presentation.theme.GrayBorder

private const val CELL_COUNT = 4

@Composable
fun ChooseSeatsScreen() {

    val state = rememberLazyGridState(initialFirstVisibleItemIndex = 0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {

        AppBar(title = stringResource(
            id = R.string.choose_seats_title)
        ) {

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            SmallDescriptionBox(
                isFreeSeat = true,
                modifier = Modifier.padding(end = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.free_seat),
                color = Color.Black,
                fontSize = 14.sp
            )

            SmallDescriptionBox(
                isFreeSeat = false,
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )

            Text(
                text = stringResource(id = R.string.occupied_seat),
                color = Color.Black,
                fontSize = 14.sp
            )
        }

        Card(
            elevation = 0.dp,
            border = BorderStroke(1.dp, GrayBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 42.dp, end = 42.dp)
        ) {

            Column(
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 44.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wheel),
                    contentDescription = "wheel",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.size(38.dp)
                )

                LazyVerticalGrid(
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    columns = GridCells.Fixed(CELL_COUNT),
                    content = {
                        items(100) { index ->
                            SeatItem(text = "${index + 1}", index = index) {

                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SeatItem(
    text: String,
    index: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 1.dp,
        border = BorderStroke(2.dp, Blue500),
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .size(40.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        )
    }
}

//private fun returnSeatModifier(index: Int) =
//    when {
//        index == 0 -> Modifier.padding(top = 24.dp)
//        index == 1 -> Modifier.padding(start = 8.dp, top = 24.dp, end = 15.dp)
//        index == 2 -> Modifier.padding(start = 15.dp, top = 24.dp)
//        index == 3 -> Modifier.padding(start = 8.dp, top = 24.dp)
//        index != 0 && index % 4 == 0 -> Modifier.padding(top = 8.dp)
//        index != 1 && index % 4 == 1 -> Modifier.padding(start = 8.dp, top = 8.dp, end = 15.dp)
//        index != 2 && index % 4 == 2 -> Modifier.padding(start = 15.dp, top = 8.dp)
//        index != 3 && index % 4 == 3 -> Modifier.padding(start = 8.dp, top = 8.dp)
//        else -> Modifier
//    }

@Composable
private fun SmallDescriptionBox(
    isFreeSeat: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        border = if (isFreeSeat) BorderStroke(1.dp, Blue500) else BorderStroke(0.dp, DarkGrayBackground),
        modifier = modifier
            .size(16.dp)
            .background(if (isFreeSeat) Color.White else DarkGrayBackground),
        content = { }
    )
}