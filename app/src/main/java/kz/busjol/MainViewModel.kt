package kz.busjol

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
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var userState by mutableStateOf(AppSettings())

    init {
        getAppSettings()
    }

    private fun getAppSettings() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect { result ->
                    userState = result
                }
        }
    }
}
