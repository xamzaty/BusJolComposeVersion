package kz.busjol.presentation.passenger.buy_ticket.choose_seats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kz.busjol.presentation.AppBar
import kz.busjol.R
import kz.busjol.presentation.ProgressButton
import kz.busjol.presentation.passenger.buy_ticket.search_journey.Ticket
import kz.busjol.presentation.theme.*
import kz.busjol.utils.rememberViewInteropNestedScrollConnection

private const val CELL_COUNT = 4

@Destination
@Composable
fun ChooseSeatsScreen(
    ticket: Ticket,
    navigator: DestinationsNavigator,
    viewModel: ChooseSeatsViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBackground),
        content = {
            item {
                AppBar(
                    title = stringResource(id = R.string.choose_seats_title)
                ) {
                    navigator.popBackStack()
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(30.dp)
                ) {
                    SmallDescriptionBox(
                        isFreeSeat = true, modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.free_seat),
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    SmallDescriptionBox(
                        isFreeSeat = false, modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.occupied_seat),
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
            item {
                Card(
                    elevation = 0.dp,
                    border = BorderStroke(1.dp, GrayBorder),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(506.dp)
                        .nestedScroll(rememberViewInteropNestedScrollConnection())
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 42.dp, end = 42.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(
                            start = 24.dp, top = 24.dp, end = 24.dp, bottom = 44.dp
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.wheel),
                            contentDescription = "wheel",
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .size(38.dp)
                        )

                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            columns = GridCells.Fixed(CELL_COUNT),
                            content = {
                                items(100) { index ->
                                    SeatItem(
                                        text = "${index + 1}",
                                        index = index
                                    ) {
                                        viewModel.onEvent(ChooseSeatsEvent.AddItemToList(index + 1))
                                    }
                                }
                            })
                    }
                }
            }
            item {
                Card(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 31.dp)
                        .offset(y = 6.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 15.dp)
                    ) {

                        Row {
                            Text(
                                text = stringResource(id = R.string.chosen_seats),
                                color = GrayText,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 5.dp)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = state.list ?: "",
                                fontWeight = FontWeight.W500,
                                fontSize = 15.sp
                            )
                        }

                        ProgressButton(
                            textId = R.string.continue_button,
                            isProgressAvailable = false,
                            isEnabled = true,
                            modifier = Modifier.padding(top = 16.dp)
                        ) {

                        }
                    }
                }
            }
        })
}

@Composable
fun SeatItem(
    modifier: Modifier = Modifier,
    text: String,
    index: Int,
    onClick: () -> Unit
) {
    Card(shape = RoundedCornerShape(4.dp),
        elevation = 1.dp,
        border = BorderStroke(2.dp, Blue500),
        modifier = modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .size(40.dp)
            .clickable { onClick() }) {
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

@Composable
private fun SmallDescriptionBox(
    isFreeSeat: Boolean, modifier: Modifier = Modifier
) {
    Card(shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        border = if (isFreeSeat) BorderStroke(1.dp, Blue500) else BorderStroke(
            0.dp, DarkGrayBackground
        ),
        modifier = modifier
            .size(16.dp)
            .background(if (isFreeSeat) Color.White else DarkGrayBackground),
        content = { })
}

private fun Modifier.returnSeatModifier(index: Int) = when (index) {
    0 -> this.padding(top = 24.dp)
    1 -> this.padding(start = 8.dp, top = 24.dp, end = 15.dp)
    2 -> this.padding(start = 15.dp, top = 24.dp)
    3 -> this.padding(start = 8.dp, top = 24.dp)
    else -> this
}
