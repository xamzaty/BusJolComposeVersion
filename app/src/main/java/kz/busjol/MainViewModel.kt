package kz.busjol

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var userState by mutableStateOf(AppSettings())

    init {
        getAppSettings()

//        if (userState.userData?.jwtToken?.isNotEmpty() == true) {
//            countDown()
//        }
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

    private fun countDown() {
        val countDownTimer = object : CountDownTimer(300000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                refreshToken()

                if (userState.userData?.jwtToken?.isNotEmpty() == true) {
                    countDown()
                }
            }
        }
        countDownTimer.start()
    }

    private fun refreshToken() {
        viewModelScope.launch {
            authRepository
                .refreshToken()
                .collect {result ->
                    when(result) {
                        is Resource.Success -> {

                        }
                        is Resource.Error -> {
                            setUnregisteredUserState()

                            setUserData(
                                UserData(
                                    id = "",
                                    name = "",
                                    email = "",
                                    phone = "",
                                    jwtToken = ""
                                )
                            )
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
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
