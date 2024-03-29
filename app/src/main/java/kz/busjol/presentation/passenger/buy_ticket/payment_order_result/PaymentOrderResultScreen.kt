package kz.busjol.presentation.passenger.buy_ticket.payment_order_result

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.busjol.R
import kz.busjol.ext.reformatDateFromBackendOnlyTime
import kz.busjol.ext.reformatDateMonthWithWords
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.BackButton
import kz.busjol.presentation.DashLine
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBackground
import kz.busjol.utils.backToMainScreen

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun PaymentOrderResultScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val pagerState = rememberPagerState()

    val data = ticket.bookingList

    var name by remember { mutableStateOf("") }
    var seatNumber by remember { mutableStateOf("") }

    val journey = ticket.journey

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            name = data?.get(page)?.clientInfo ?: ""
            seatNumber = data?.get(page)?.seatNumber ?: ""
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
            .verticalScroll(scrollState)
    ) {

        AppBar(title = stringResource(id = R.string.payment_order_result_title, "123123"), isCross = true) {
            scope.launch {
                navigator.backToMainScreen()
            }
        }

        Card(
            elevation = 0.dp,
            border = BorderStroke(15.dp, Blue500),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(start = 45.dp, top = 16.dp, end = 45.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                val (clientDataRow, seatsRow, fromCircle, fromCityColumn, toCircle,
                    toCityColumn, verticalLine, qrLazyRow,
                    horizontalDivider, circleLeft, circleRight, counter) = createRefs()

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .constrainAs(clientDataRow) {
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    }) {

                    Text(
                        text = stringResource(id = R.string.client_data), fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = name,
                        fontWeight = FontWeight.W700,
                        fontSize = 11.sp
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .constrainAs(seatsRow) {
                        top.linkTo(clientDataRow.bottom, 12.dp)
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    }) {

                    Text(
                        text = stringResource(id = R.string.seats), fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = seatNumber,
                        fontWeight = FontWeight.W700,
                        fontSize = 11.sp
                    )
                }

                Image(painter = painterResource(id = R.drawable.circle),
                    contentDescription = "circle",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(16.dp)
                        .constrainAs(fromCircle) {
                            start.linkTo(parent.start, 31.dp)
                            top.linkTo(seatsRow.bottom, 16.dp)
                        })

                Image(painter = painterResource(id = R.drawable.countour_vertical_line),
                    contentDescription = "line",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(76.dp)
                        .constrainAs(verticalLine) {
                            start.linkTo(fromCircle.start)
                            top.linkTo(fromCircle.bottom)
                            end.linkTo(fromCircle.end)
                        })

                Image(painter = painterResource(id = R.drawable.circle),
                    contentDescription = "circle",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(16.dp)
                        .constrainAs(toCircle) {
                            start.linkTo(fromCircle.start)
                            top.linkTo(verticalLine.bottom)
                            end.linkTo(fromCircle.end)
                        })

                Column(modifier = Modifier.constrainAs(fromCityColumn) {
                    top.linkTo(fromCircle.top)
                    start.linkTo(fromCircle.end, 8.dp)
                }) {

                    Text(
                        text =
                        "${journey?.departureTime?.reformatDateFromBackendOnlyTime()}, ${journey?.departureTime?.reformatDateMonthWithWords()}",
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                        fontSize = 13.sp
                    )

                    Text(
                        text = ticket.journey?.cityFrom?.name ?: "",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = ticket.journey?.stopName ?: "",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(modifier = Modifier.constrainAs(toCityColumn) {
                    top.linkTo(toCircle.top)
                    start.linkTo(toCircle.end, 8.dp)
                }) {

                    Text(
                        text =
                        "${journey?.arrivalTime?.reformatDateFromBackendOnlyTime()}, ${journey?.arrivalTime?.reformatDateMonthWithWords()}",
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                        fontSize = 13.sp
                    )

                    Text(
                        text = ticket.journey?.cityTo?.name ?: "",
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = "Автовокзал Балхаш",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.blue_circle_left),
                    contentDescription = "circleLeft",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.constrainAs(circleLeft) {
                        start.linkTo(parent.start, 10.dp)
                        top.linkTo(toCityColumn.bottom, 24.dp)
                    }
                )

                DashLine(
                    modifier = Modifier.constrainAs(horizontalDivider) {
                        start.linkTo(circleLeft.end)
                        end.linkTo(circleRight.start)
                        top.linkTo(circleLeft.top, 2.dp)
                        bottom.linkTo(circleLeft.bottom)
                        width = Dimension.fillToConstraints
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.blue_circle_right),
                    contentDescription = "circleRight",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.constrainAs(circleRight) {
                        end.linkTo(parent.end, 10.dp)
                        top.linkTo(circleLeft.top)
                        bottom.linkTo(circleLeft.bottom)
                    }
                )

                HorizontalPager(
                    modifier = Modifier.constrainAs(qrLazyRow) {
                        top.linkTo(horizontalDivider.bottom, 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(
                            if (data?.size == 1) parent.bottom
                            else counter.top
                        )
                    }, count = data?.size ?: 1, state = pagerState
                ) {
                    ticket.bookingList?.forEach {
                        Image(
                            bitmap = encodeAsBitmap(it.qrCode),
                            contentDescription = "qr",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if ((data?.size ?: 1) > 1) {
                    CounterRow(
                        totalPages = data?.size ?: 1,
                        pagerState = pagerState,
                        modifier = Modifier.constrainAs(counter) {
                            top.linkTo(qrLazyRow.bottom, 19.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, 37.dp)
                        },
                        scope = scope
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            elevation = 10.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 29.dp)
                .offset(y = 8.dp)
        ) {
            ProgressButton(
                textId = R.string.back_to_main_menu_button,
                progressBarActiveState = false,
                enabled = true,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 15.dp)
            ) {
                scope.launch {
                    navigator.backToMainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun CounterRow(
    modifier: Modifier = Modifier,
    totalPages: Int,
    pagerState: PagerState,
    scope: CoroutineScope
) {
    var currentPage = pagerState.currentPage + 1

    Row(
        modifier = modifier
    ) {

        BackButton(
            modifier = Modifier
        ) {
            if (currentPage > 1) {
                currentPage--
                scope.launch {
                    pagerState.animateScrollToPage(currentPage-1)
                }
            }
        }

        Text(
            text = "$currentPage/$totalPages",
            fontSize = 17.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 25.dp)
        )

        BackButton(modifier = Modifier
            .padding(start = 25.dp)
            .graphicsLayer {
                rotationZ = 180f
            }) {
            if (currentPage < totalPages) {
                currentPage++
                scope.launch {
                    pagerState.animateScrollToPage(currentPage-1)
                }
            }
        }
    }
}

@Throws(WriterException::class)
private fun encodeAsBitmap(str: String?) =
    BarcodeEncoder().encodeBitmap(str, BarcodeFormat.QR_CODE, 450, 450).asImageBitmap()
