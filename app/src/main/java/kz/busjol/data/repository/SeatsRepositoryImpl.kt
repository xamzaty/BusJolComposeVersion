package kz.busjol.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toSeatsList
import kz.busjol.data.remote.api.SeatsListApi
import kz.busjol.domain.models.Seats
import kz.busjol.domain.repository.SeatsListRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SeatsRepositoryImpl @Inject constructor(
    private val seatsListApi: SeatsListApi
): SeatsListRepository {

    override suspend fun getSeatsList(id: String): Flow<Resource<List<Seats>>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                emit(Resource.Loading(false))
                emit(
                    Resource.Success(
                        data = seatsListApi.getSeatsList(id).toSeatsList()
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