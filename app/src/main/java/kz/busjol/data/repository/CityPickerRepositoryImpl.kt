package kz.busjol.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toCityList
import kz.busjol.data.remote.api.CityListApi
import kz.busjol.domain.models.City
import kz.busjol.domain.repository.CityListRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CityPickerRepositoryImpl @Inject constructor(
    private val cityListApi: CityListApi
): CityListRepository{

    override suspend fun getCityList(): Flow<Resource<List<City>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                emit(Resource.Loading(false))
                emit(
                    Resource.Success(
                        data = cityListApi.getCityList().toCityList()
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