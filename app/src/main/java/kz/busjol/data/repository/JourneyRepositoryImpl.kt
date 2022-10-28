package kz.busjol.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toJourneyList
import kz.busjol.data.remote.JourneyPost
import kz.busjol.data.remote.SearchJourneyApi
import kz.busjol.domain.models.Journey
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class JourneyRepositoryImpl @Inject constructor(
    private val journeyListApi: SearchJourneyApi
): JourneyListRepository {

    override suspend fun getJourneyList(journeyPost: JourneyPost): Flow<Resource<List<Journey>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                emit(Resource.Loading(false))
                emit(
                    Resource.Success(
                        data = journeyListApi.getJourneyList(journeyPost).toJourneyList()
                    )
                )
            } catch(e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(false))
            }
        }
    }
}