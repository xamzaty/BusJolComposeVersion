package kz.busjol.data.repository

import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toJourneyList
import kz.busjol.data.remote.api.JourneysApi
import kz.busjol.domain.repository.JourneysRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class JourneysRepositoryImpl @Inject constructor(
    private val journeysApi: JourneysApi
): JourneysRepository {

    override suspend fun getDriverJourneys(driverId: String) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = journeysApi.getDriverJourneys(driverId).toJourneyList()
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