package kz.busjol.presentation.passenger.search_journey.calendar

import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import kz.busjol.presentation.passenger.search_journey.SearchJourneyViewModel
import kz.busjol.R
import kz.busjol.presentation.passenger.search_journey.SearchJourneyEvent
import kz.busjol.presentation.theme.GrayBorder

@Composable
fun CalendarScreen(
    onCloseBottomSheet: () -> Unit,
    viewModel: SearchJourneyViewModel = hiltViewModel()
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
                    .clickable { onCloseBottomSheet() }
            )

            Text(text = stringResource(id = R.string.calendar_date_title),
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

        AndroidView( {
            CalendarView(
                ContextThemeWrapper(it, R.style.Widget_CalendarView_Custom).apply {
                    firstDayOfWeekFromLocale()
                }
            ).apply {
                minDate = System.currentTimeMillis() - 1000

                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val date =
                        if (month.toString().length == 1) "$dayOfMonth.0$month.$year"
                        else "$dayOfMonth.$month.$year"

                    viewModel.onEvent(SearchJourneyEvent.UpdateDateValue(date))
                    onCloseBottomSheet()
                }
            }
        },
            modifier = Modifier.fillMaxWidth()
        )
    }
}