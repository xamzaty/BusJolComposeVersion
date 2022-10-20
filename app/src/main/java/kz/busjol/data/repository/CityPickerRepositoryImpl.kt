package kz.busjol.data.repository

import kz.busjol.data.mappers.toCityList
import kz.busjol.data.remote.CityListApi
import kz.busjol.domain.models.City
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.util.Resource
import javax.inject.Inject

class CityPickerRepositoryImpl @Inject constructor(
    private val cityListApi: CityListApi
): CityListRepository{

    override suspend fun getCityList(): Resource<List<City>> {
        return try {
            Resource.Success(
                data = cityListApi.getCityList().toCityList()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}