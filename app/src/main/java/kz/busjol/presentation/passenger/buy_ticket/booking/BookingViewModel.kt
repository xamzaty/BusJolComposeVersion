package kz.busjol.presentation.passenger.buy_ticket.booking

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(BookingScreenState())
        private set

    init {
        countDown()
    }

    private fun countDown() {
        val countDownTimer = object : CountDownTimer(600000, 1000) {
            override fun onTick(p0: Long) {
                val millis: Long = p0
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

            override fun onFinish() {
                state = state.copy(
                    isTimeExpired = true
                )
            }
        }
        countDownTimer.start()
    }
}
