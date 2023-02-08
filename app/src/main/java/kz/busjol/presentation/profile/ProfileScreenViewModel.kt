package kz.busjol.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.Language
import kz.busjol.UserData
import kz.busjol.UserState
import kz.busjol.data.remote.LogoutPost
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    private var token: String? = null

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.SetLanguage -> {
                setLanguage(event.language)
                getDataStoreValues()
            }
            is ProfileEvent.SetNotificationStatus -> {
                setNotificationStatus(event.notificationStatus)
                getDataStoreValues()
            }
            ProfileEvent.ExitUserState -> {
                logoutUser()
                getDataStoreValues()
            }
        }
    }

    init {
        getDataStoreValues()
    }

    private fun logoutUser() {
        viewModelScope.launch {
            setUnregisteredUserState()
            setUserData(
                UserData(
                    id = "",
                    email = "",
                    name = "",
                    phone = "",
                    jwtToken = ""
                )
            )

            authRepository
                .logoutUser(LogoutPost(token = token ?: ""))
                .collect {result ->
                    when(result) {
                        is Resource.Success -> {
                            setUnregisteredUserState()

                            state = state.copy(
                                isLoading = false,
                                error = null
                            )

                            setUserData(
                                UserData(
                                    id = "",
                                    email = "",
                                    name = "",
                                    phone = "",
                                    jwtToken = ""
                                )
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
                                isLoading = result.isLoading,
                            )
                        }
                    }
                }
        }
    }

    private fun getDataStoreValues() {
        viewModelScope.launch {
            dataStoreRepository
                .getAppSettings()
                .collect {
                    token = it.userData?.jwtToken

                    state = state.copy(
                        emailValue = it.userData?.email,
                        language = it.language,
                        userState = it.userState,
                        isNotificationsEnabled = it.isNotificationsAvailable
                    )
                }
        }
    }

    private fun setLanguage(language: Language) {
        viewModelScope.launch {
            dataStoreRepository.setLanguage(language)
        }
    }

    private fun setNotificationStatus(notificationStatus: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setNotificationsState(notificationStatus)
        }
    }

    private fun setUnregisteredUserState() {
        viewModelScope.launch {
            dataStoreRepository.setUserState(UserState.UNREGISTERED)
        }
    }

    private fun setUserData(userData: UserData) {
        viewModelScope.launch {
            dataStoreRepository.setUserData(userData)
        }
    }
}
