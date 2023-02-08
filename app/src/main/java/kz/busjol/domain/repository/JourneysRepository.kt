package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.domain.models.Journey
import kz.busjol.domain.util.Resource

interface JourneysRepository {

    suspend fun getDriverJourneys(driverId: String) : Flow<Resource<List<Journey>>>
}