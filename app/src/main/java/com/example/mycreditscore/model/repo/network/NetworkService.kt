package com.example.mycreditscore.model.repo.network

import com.example.mycreditscore.model.CreditScoreResponse
import retrofit2.http.GET

interface NetworkService {
    @GET("endpoint.json")
    suspend fun getCreditScore(): CreditScoreResponse?
}