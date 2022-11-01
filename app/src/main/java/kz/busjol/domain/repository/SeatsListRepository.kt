package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.domain.models.Seats
import kz.busjol.domain.util.Resource

interface SeatsListRepository {
    suspend fun getSeatsList(id: String): Flow<Resource<List<Seats>>>
}