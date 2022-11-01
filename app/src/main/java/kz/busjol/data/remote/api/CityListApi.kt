package kz.busjol.data.remote.api

import kz.busjol.data.remote.CityDto
import retrofit2.http.GET

interface CityListApi {

    @GET("cities")
    suspend fun getCityList(): List<CityDto>
}