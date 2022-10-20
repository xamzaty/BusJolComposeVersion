package kz.busjol.data.remote

import retrofit2.http.GET

interface CityListApi {

    @GET("cities")
    suspend fun getCityList(): List<CityDto>
}