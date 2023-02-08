package kz.busjol.presentation.passenger.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.UserData
import kz.busjol.UserState
import kz.busjol.data.remote.AuthenticatePost
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnLoginButtonPressed ->
                authenticateUser(event.authenticatePost)
        }
    }

    private fun authenticateUser(body: AuthenticatePost) {
        viewModelScope.launch {
            authRepository
                .authenticateUser(body)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            val data = result.data
                            val role = data?.getToken()?.getResultFromJson()?.role

                            if (role == "driver") {
                                setUserState(UserState.DRIVER)
                            } else if (role?.isNotEmpty() == true) {
                                setUserState(UserState.REGISTERED)
                                state = state.copy(returnToMainScreen = true)
                            }

                            setUserData(
                                UserData(
                                    id = data?.getToken()?.getResultFromJson()?.id ?: "",
                                    name = data?.name ?: "",
                                    email = data?.email ?: "",
                                    phone = data?.phone ?: "",
                                    jwtToken = data?.jwtToken ?: ""
                                )
                            )

                            state = state.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                isLoading = false,
                                error = result.message
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
    private fun setUserState(userState: UserState) {
        viewModelScope.launch {
            dataStoreRepository.setUserState(userState)
        }
    }

    private fun setUserData(userData: UserData) {
        viewModelScope.launch {
            dataStoreRepository.setUserData(userData)
        }
    }

    private fun String.getResultFromJson() =
        Gson().fromJson(this, TokenParseModel::class.java)
}
