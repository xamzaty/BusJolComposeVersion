package kz.busjol.presentation.passenger.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.busjol.UserData
import kz.busjol.UserState
import kz.busjol.data.remote.AuthenticatePost
import kz.busjol.domain.models.Authenticate
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.repository.DataStoreRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

private const val DRIVER = "driver"
private const val ROLE = "role"
private const val ID = "id"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnLoginButtonPressed -> authenticateUser(event.authenticatePost)
        }
    }

    private fun authenticateUser(body: AuthenticatePost) {
        viewModelScope.launch {
            authRepository.authenticateUser(body).collect { result ->
                when (result) {
                    is Resource.Success -> handleSuccess(result)
                    is Resource.Error -> handleError(result)
                    is Resource.Loading -> handleLoading(result)
                }
            }
        }
    }

    private fun handleSuccess(result: Resource.Success<Authenticate>) {
        val data = result.data
        val decodedToken: DecodedJWT = JWT.decode(data?.jwtToken)

        println("Decoded Token Claims: ${decodedToken.claims}")

        val role = decodedToken.getClaim(ROLE).asString()
        val id = decodedToken.getClaim(ID).asString() ?: ""

        if (role.contains(DRIVER)) {
            setUserState(UserState.DRIVER)
        } else if (role.isNotEmpty()) {
            setUserState(UserState.REGISTERED)
            state = state.copy(returnToMainScreen = true)
        }

        setUserData(
            UserData(
                id = id,
                name = data?.name.orEmpty(),
                email = data?.email.orEmpty(),
                phone = data?.phone.orEmpty(),
                jwtToken = data?.jwtToken.orEmpty()
            )
        )

        state = state.copy(isLoading = false, error = null)
    }

    private fun handleError(result: Resource.Error<Authenticate>) {
        state = state.copy(isLoading = false, error = result.message)
    }

    private fun handleLoading(result: Resource.Loading<Authenticate>) {
        state = state.copy(isLoading = result.isLoading)
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
}
