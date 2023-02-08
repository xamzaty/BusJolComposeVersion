package kz.busjol.data.repository

import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toAuthenticateBody
import kz.busjol.data.remote.AuthenticatePost
import kz.busjol.data.remote.LogoutPost
import kz.busjol.data.remote.RegisterPost
import kz.busjol.data.remote.RestorePasswordPost
import kz.busjol.data.remote.api.AuthApi
import kz.busjol.domain.repository.AuthRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun authenticateUser(body: AuthenticatePost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = authApi.authenticateUser(body).toAuthenticateBody()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            }
        }

    override suspend fun refreshToken() =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = authApi.refreshToken()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            }
        }

    override suspend fun registerUser(body: RegisterPost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = authApi.registerUser(body)
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            }
        }

    override suspend fun restorePassword(body: RestorePasswordPost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = authApi.restorePassword(body)
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            }
        }

    override suspend fun logoutUser(body: LogoutPost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = authApi.logoutUser(body)
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("${e.message}"))
            }
        }
}
