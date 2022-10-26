package kz.busjol.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface SearchJourneyApi {

    @POST("search")
    suspend fun getJourneyList(
        @Body body: JourneyPost
    ): List<JourneyDto>
}