package kz.busjol.data.remote.api

import kz.busjol.data.remote.JourneyDto
import retrofit2.http.GET
import retrofit2.http.Query

interface JourneysApi {

    @GET("Journeys/Driver/GetJourneys")
    suspend fun getDriverJourneys(
        @Query("driverId") driverId: String
    ): List<JourneyDto>
}