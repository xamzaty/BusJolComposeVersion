package kz.busjol.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.Language
import kz.busjol.domain.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.SetLanguage -> {
                setLanguage(event.language)
                getDataStoreValues()
            }
        }
    }

    init {
        getDataStoreValues()
    }

    private fun getDataStoreValues() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    state = state.copy(
                        language = it.language,
                        userState = it.userState
                    )
                }
        }
    }

    private fun setLanguage(language: Language) {
        viewModelScope.launch {
            dataStoreRepository.setLanguage(language)
        }
    }
}