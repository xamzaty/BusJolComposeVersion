package kz.busjol.data.remote.api

import kz.busjol.data.remote.JourneyDto
import kz.busjol.data.remote.JourneyPost
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchJourneyApi {

    @POST("search")
    suspend fun getJourneyList(
        @Body body: JourneyPost
    ): List<JourneyDto>
}