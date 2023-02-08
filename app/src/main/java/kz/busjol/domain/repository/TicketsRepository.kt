package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.domain.util.Resource

interface TicketsRepository {

    suspend fun checkTicket(qrCode: String): Flow<Resource<Unit>>
}