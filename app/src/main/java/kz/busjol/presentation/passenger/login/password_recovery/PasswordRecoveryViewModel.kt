package kz.busjol.presentation.passenger.login.password_recovery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.data.remote.RestorePasswordPost
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(PasswordRecoveryState())
        private set

    fun onEvent(event: PasswordRecoveryEvent) {
        when(event) {
            is PasswordRecoveryEvent.OnSendButtonClicked -> {
                recoverPassword(event.body)
            }
        }
    }

    private fun recoverPassword(body: RestorePasswordPost) {
        viewModelScope.launch {
            authRepository
                .restorePassword(body)
                .collect {result ->
                    when(result) {
                        is Resource.Success -> {
                            state = state.copy(
                                isPasswordRecoverySuccess = true,
                                error = state.error,
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isPasswordRecoverySuccess = false,
                                error = result.message,
                                isLoading = false
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }
}