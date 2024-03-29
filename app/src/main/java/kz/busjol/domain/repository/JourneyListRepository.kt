package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.data.remote.JourneyPost
import kz.busjol.domain.models.Journey
import kz.busjol.domain.util.Resource

interface JourneyListRepository {
    suspend fun getJourneyList(journeyPost: JourneyPost): Flow<Resource<List<Journey>>>
}