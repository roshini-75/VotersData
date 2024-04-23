package com.example.votersdata

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("leader/getVillageLevelVotersData")
    suspend fun getVoterData(@Body request: GetVoterDataRequest): List<VoterData>
}

data class GetVoterDataRequest(
    val constituency: String,
    val fromSNO: Int,
    val pbNo: String,
    val status: String,
    val toSNO: Int
)
