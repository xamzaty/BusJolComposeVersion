package kz.busjol.presentation.passenger.buy_ticket.booking

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class BookingViewModel @Inject constructor(): ViewModel() {

    var state by mutableStateOf(BookingScreenState())
        private set

    init {
        countDown()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun countDown() {
        val expireDate = LocalDateTime.now().plusSeconds(600).toEpochSecond(ZoneOffset.UTC)
        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        tickerFlow(expireDate - currentTime)
            .onEach { secondsRemaining ->
                val millis: Long = secondsRemaining * 1000

                val hms = String.format(
                    "%02d:%02d",
                    (TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millis)
                    ))
                )

                state = state.copy(
                    countdownTimerValue = hms
                )
            }
            .launchIn(viewModelScope)
    }

    private fun tickerFlow(start: Long, end: Long = 0L) = flow {
        for (i in start downTo end) {
            emit(i)
            delay(1_000)
        }
    }
}