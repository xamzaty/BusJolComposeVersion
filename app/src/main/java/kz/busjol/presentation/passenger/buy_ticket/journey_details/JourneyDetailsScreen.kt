package kz.busjol.presentation.passenger.buy_ticket.journey_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.presentation.theme.GrayBorder
import kz.busjol.presentation.theme.GrayText
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JourneyDetailsScreen(
    sheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    Column(
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
                    .clickable { coroutineScope.launch { sheetState.hide() } }
            )

            Text(
                text = stringResource(id = R.string.journey_details_title),
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

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 16.dp, bottom = 24.dp)
        ) {
            val (topColumn, middleColumn, departureColumn,
                arrivalColumn, departureImage, line,
                arrivalImage, fromCity, toCity) = createRefs()

            Column(
                modifier = Modifier.constrainAs(topColumn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            ) {
                Text(
                    text = "Алматы - Балхаш",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp
                )

                Text(
                    text = stringResource(id = R.string.time_on_the_way, "13", "30"),
                    color = GrayText,
                    fontWeight = FontWeight.W500,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Column(
                modifier = Modifier.constrainAs(middleColumn) {
                    top.linkTo(topColumn.bottom, 17.dp)
                    start.linkTo(parent.start)
                }
            ) {

                Text(
                    text = stringResource(id = R.string.journey_number, "23"),
                    color = Color(0xFF698CA5),
                    fontSize = 12.sp
                )

                Text(
                    text = stringResource(id = R.string.carrier, "Автовокзал Алматы"),
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.constrainAs(departureColumn) {
                    top.linkTo(middleColumn.bottom, 17.dp)
                    start.linkTo(parent.start)
                }
            ) {

                Text(
                    text = "09:00",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = "12 декабря",
                    fontSize = 10.sp,
                    color = Color.Black,
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.constrainAs(arrivalColumn) {
                    top.linkTo(departureColumn.bottom, 30.dp)
                    start.linkTo(parent.start)
                }
            ) {

                Text(
                    text = "14:20",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500
                )

                Text(
                    text = "13 декабря",
                    fontSize = 10.sp,
                    color = Color.Black,
                )
            }

            Image(
                painter = painterResource(id = R.drawable.from_city),
                contentDescription = "departureImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.constrainAs(departureImage) {
                    top.linkTo(departureColumn.top)
                    start.linkTo(departureColumn.end, 16.dp)
                }
            )

            Image(
                painter = painterResource(id = R.drawable.to_city),
                contentDescription = "arrivalImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.constrainAs(arrivalImage) {
                    top.linkTo(arrivalColumn.top, 1.dp)
                    start.linkTo(departureImage.start)
                    end.linkTo(departureImage.end)
                }
            )

            Divider(
                color = Color(0xFF446883),
                modifier = Modifier
                    .width(1.dp)
                    .constrainAs(line) {
                        top.linkTo(departureImage.bottom, 1.dp)
                        start.linkTo(departureImage.start)
                        end.linkTo(departureImage.end)
                        bottom.linkTo(arrivalImage.top)
                        height = Dimension.fillToConstraints
                    }
            )

            Text(
                text = "Алматы".uppercase(Locale.ROOT),
                color = Color.Black,
                fontWeight = FontWeight.W500,
                modifier = Modifier.constrainAs(fromCity) {
                    top.linkTo(departureImage.top)
                    start.linkTo(departureImage.end, 16.dp)
                    bottom.linkTo(departureImage.bottom)
                }
            )

            Text(
                text = "Балхаш".uppercase(Locale.ROOT),
                color = Color.Black,
                fontWeight = FontWeight.W500,
                modifier = Modifier.constrainAs(toCity) {
                    top.linkTo(arrivalImage.top)
                    start.linkTo(arrivalImage.end, 16.dp)
                    bottom.linkTo(arrivalImage.bottom)
                }
            )
        }
    }
}