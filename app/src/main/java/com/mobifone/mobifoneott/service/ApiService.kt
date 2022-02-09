package com.mobifone.mobifoneott.service

import com.example.webrtcdemoandroid.constants.ApiClient
import com.example.webrtcdemoandroid.constants.ApiConstants
import com.mobifone.mobifoneott.network.token.TokenResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("callcenter-backend/token_msale/{name}")
    suspend fun getToken(
        @Path("name") name: String
    ): TokenResponse


    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(ApiConstants.baseUrl)
                .client(ApiClient.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}