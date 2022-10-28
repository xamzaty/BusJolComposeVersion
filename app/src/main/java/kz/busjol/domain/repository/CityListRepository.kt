package kz.busjol.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.busjol.domain.models.City
import kz.busjol.domain.util.Resource

interface CityListRepository {
    suspend fun getCityList(): Flow<Resource<List<City>>>
}