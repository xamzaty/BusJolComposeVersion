package kz.busjol.presentation.passenger.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.UserState
import kz.busjol.domain.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.SetUserState -> setUserState(event.value)
        }
    }

    private fun setUserState(userState: UserState) {
        viewModelScope.launch {
            dataStoreRepository.setUserState(userState)
        }
    }
}