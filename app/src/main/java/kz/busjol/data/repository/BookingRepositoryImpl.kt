package kz.busjol.data.repository

import kotlinx.coroutines.flow.flow
import kz.busjol.data.mappers.toBookingList
import kz.busjol.data.remote.BookingPost
import kz.busjol.data.remote.api.BookingApi
import kz.busjol.domain.repository.BookingListRepository
import kz.busjol.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingListApi: BookingApi
): BookingListRepository {

    override suspend fun getBookingList(bookingPost: BookingPost) =
        flow {
            emit(Resource.Loading(true))
            try {
                emit(
                    Resource.Success(
                        data = bookingListApi.getBookingItem(bookingPost).toBookingList()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Ошибка: ${e.printStackTrace()}"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Ошибка: ${e.printStackTrace()}"))
            }
        }
}