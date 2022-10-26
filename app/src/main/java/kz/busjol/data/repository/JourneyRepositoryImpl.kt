package kz.busjol.data.repository

import kz.busjol.data.mappers.toJourneyList
import kz.busjol.data.remote.JourneyPost
import kz.busjol.data.remote.SearchJourneyApi
import kz.busjol.domain.models.Journey
import kz.busjol.domain.repository.JourneyListRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

class JourneyRepositoryImpl @Inject constructor(
    private val journeyListApi: SearchJourneyApi
): JourneyListRepository {

    override suspend fun getJourneyList(journeyPost: JourneyPost): Resource<List<Journey>> {
        return try {
            Resource.Success(
                data = journeyListApi.getJourneyList(journeyPost).toJourneyList()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}