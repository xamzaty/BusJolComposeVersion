package kz.busjol.data.remote.api

import kz.busjol.data.remote.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("Users/authenticate")
    suspend fun authenticateUser(@Body body: AuthenticatePost): AuthenticateDto

    @POST("Users/refresh-token")
    suspend fun refreshToken()

    @POST("Users/register")
    suspend fun registerUser(@Body body: RegisterPost)

    @POST("Users/restore-password")
    suspend fun restorePassword(@Body body: RestorePasswordPost)

    @POST("Users/logout")
    suspend fun logoutUser(@Body body: LogoutPost)
}
