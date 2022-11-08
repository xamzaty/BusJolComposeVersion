package kz.busjol.presentation.passenger.my_tickets

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.domain.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MyTicketsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    var state by mutableStateOf(MyTicketsState())
        private set

    init {
        getDataStoreValues()
    }

    private fun getDataStoreValues() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    state = state.copy(
                        userState = it.userState
                    )
                }
        }
    }
}