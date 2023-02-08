package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.data.remote.AuthenticatePost
import kz.busjol.data.remote.LogoutPost
import kz.busjol.data.remote.RegisterPost
import kz.busjol.data.remote.RestorePasswordPost
import kz.busjol.domain.models.Authenticate
import kz.busjol.domain.util.Resource
import retrofit2.http.Body

interface AuthRepository {

    suspend fun authenticateUser(@Body body: AuthenticatePost): Flow<Resource<Authenticate>>

    suspend fun refreshToken(): Flow<Resource<Unit>>

    suspend fun registerUser(@Body body: RegisterPost): Flow<Resource<Unit>>

    suspend fun restorePassword(@Body body: RestorePasswordPost): Flow<Resource<Unit>>

    suspend fun logoutUser(@Body body: LogoutPost): Flow<Resource<Unit>>
}