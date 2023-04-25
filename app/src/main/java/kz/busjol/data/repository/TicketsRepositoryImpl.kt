package kz.busjol.data.repository

import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toTicketInfoDomain
import kz.busjol.data.remote.api.TicketsApi
import kz.busjol.domain.repository.TicketsRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TicketsRepositoryImpl @Inject constructor(
    private val ticketsApi: TicketsApi
): TicketsRepository {

    override suspend fun checkTicket(qrCode: String) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = ticketsApi.checkTicket(qrCode).toTicketInfoDomain()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Ошибка: ${e.printStackTrace()}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Ошибка: ${e.printStackTrace()}"))
            }
        }
}
