package kz.busjol.presentation.passenger.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.SetUserState -> setUserState(event.value)
            is LoginEvent.GetEmailValue -> setEmailValue(event.value)
            is LoginEvent.GetPasswordValue -> setPasswordValue(event.value)
        }
    }

    private fun setEmailValue(value: String) {
        state = state.copy(
            emailValue = value
        )
    }

    private fun setPasswordValue(value: String) {
        state = state.copy(
            passwordValue = value
        )
    }

    private fun setUserState(userState: UserState) {
        viewModelScope.launch {
            dataStoreRepository.setUserState(userState)
        }
    }
}