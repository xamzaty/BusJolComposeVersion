package kz.busjol.presentation.passenger.login.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.data.remote.RegisterPost
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(RegistrationState())
        private set

    fun onEvent(event: RegistrationEvent) {
        when(event) {
            is RegistrationEvent.OnRegisterButtonClicked -> {
                registerUser(event.body)
            }
        }
    }

    private fun registerUser(body: RegisterPost) {
        viewModelScope.launch {
            authRepository
                .registerUser(body)
                .collect {result ->
                    when(result) {
                        is Resource.Success -> {
                            state = state.copy(
                                isRegistrationSuccess = true,
                                isLoading = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isRegistrationSuccess = false,
                                isLoading = false,
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}