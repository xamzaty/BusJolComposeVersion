package kz.busjol.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toJourneyList
import kz.busjol.data.remote.JourneyPost
import kz.busjol.data.remote.api.SearchJourneyApi
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class JourneyRepositoryImpl @Inject constructor(
    private val journeyListApi: SearchJourneyApi
) : JourneyListRepository {

    override suspend fun getJourneyList(journeyPost: JourneyPost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = journeyListApi.getJourneyList(journeyPost).toJourneyList()
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