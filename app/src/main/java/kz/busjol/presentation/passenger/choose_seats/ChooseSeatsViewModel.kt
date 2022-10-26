package kz.busjol.presentation.passenger.choose_seats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseSeatsViewModel @Inject constructor(

): ViewModel() {

    var state by mutableStateOf(ChooseSeatsState())
        private set


}