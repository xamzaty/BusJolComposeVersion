package kz.busjol.presentation.passenger.buy_ticket.payment_order_result

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kz.busjol.R
import kz.busjol.presentation.AppBar
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.Blue500
import kz.busjol.presentation.theme.GrayBackground

@Destination
@Composable
fun PaymentOrderResultScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground)
    ) {

        val clientName = remember { "Khamzat Yerzhanov" }
        val departureDate = remember { "19:10, 24 декабря" }
        val arrivalDate = remember { "15:00, 25 декабря" }
        val departureCity = remember { "Алматы" }
        val arrivalCity = remember { "Балхаш" }
        val departureStation = remember { "Автовокзал Сайран" }
        val arrivalStation = remember { "Автовокзал Балхаш" }
        val qrUrlList = remember { "https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code" }

        val seatList = remember { mutableListOf(11) }

        seatList.add(12)
        seatList.add(13)

        AppBar(title = stringResource(id = R.string.payment_order_result_title, "123123")) {

        }
        
        Card(
            elevation = 0.dp,
            border = BorderStroke(15.dp, Blue500),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(start = 15.dp, top = 16.dp, end = 15.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                val (clientDataRow, seatsRow, fromCircle, fromCityColumn,
                    toCircle, toCityColumn, verticalLine, qrLazyRow, horizontalDivider) = createRefs()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .constrainAs(clientDataRow) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                        }
                ) {

                    Text(
                        text = stringResource(id = R.string.client_data),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = clientName,
                        fontWeight = FontWeight.W700,
                        fontSize = 11.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp)
                        .constrainAs(seatsRow) {
                            top.linkTo(clientDataRow.bottom, 12.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                        }
                ) {

                    Text(
                        text = stringResource(id = R.string.seats),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = seatList.toString(),
                        fontWeight = FontWeight.W700,
                        fontSize = 11.sp
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = "circle",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(16.dp)
                        .constrainAs(fromCircle) {
                            start.linkTo(parent.start, 31.dp)
                            top.linkTo(seatsRow.bottom, 16.dp)
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.countour_vertical_line),
                    contentDescription = "line",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(76.dp)
                        .constrainAs(verticalLine) {
                            start.linkTo(fromCircle.start)
                            top.linkTo(fromCircle.bottom)
                            end.linkTo(fromCircle.end)
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.circle),
                    contentDescription = "circle",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(16.dp)
                        .constrainAs(toCircle) {
                            start.linkTo(fromCircle.start)
                            top.linkTo(verticalLine.bottom)
                            end.linkTo(fromCircle.end)
                        }
                )

                Column(
                    modifier = Modifier.constrainAs(fromCityColumn) {
                        top.linkTo(fromCircle.top)
                        start.linkTo(fromCircle.end, 8.dp)
                    }
                ) {

                    Text(
                        text = departureDate,
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                        fontSize = 13.sp
                    )

                    Text(
                        text = departureCity,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = departureStation,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(
                    modifier = Modifier.constrainAs(toCityColumn) {
                        top.linkTo(toCircle.top)
                        start.linkTo(toCircle.end, 8.dp)
                    }
                ) {

                    Text(
                        text = arrivalDate,
                        fontWeight = FontWeight.W700,
                        color = Color.Black,
                        fontSize = 13.sp
                    )

                    Text(
                        text = arrivalCity,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Text(
                        text = arrivalStation,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.countour_horizontal_line),
                    contentDescription = "horizontal_line",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.constrainAs(horizontalDivider) {
                        start.linkTo(parent.start, 10.dp)
                        end.linkTo(parent.end, 10.dp)
                        top.linkTo(toCityColumn.bottom, 24.dp)
                        width = Dimension.fillToConstraints
                    }
                )

                Image(
                    bitmap = encodeAsBitmap("https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code"),
                    contentDescription = "qr",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .constrainAs(qrLazyRow) {
                            top.linkTo(horizontalDivider.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }



}

@Throws(WriterException::class)
fun encodeAsBitmap(str: String?): ImageBitmap {
    val qrCodeBitmap = BarcodeEncoder().encodeBitmap(str, BarcodeFormat.QR_CODE, 450, 450)
    return qrCodeBitmap.asImageBitmap()
}