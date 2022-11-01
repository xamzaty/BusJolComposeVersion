package kz.busjol.data.remote.api

import kz.busjol.data.remote.SeatsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SeatsListApi {

    @GET("Seats/journey/{id}")
    suspend fun getSeatsList(@Path("id") id: String): List<SeatsDto>
}