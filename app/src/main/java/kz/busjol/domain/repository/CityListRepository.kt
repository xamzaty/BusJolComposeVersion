package kz.busjol.domain.repository

import kz.busjol.domain.models.City
import kz.busjol.domain.util.Resource

interface CityListRepository {
    suspend fun getCityList(): Resource<List<City>>
}