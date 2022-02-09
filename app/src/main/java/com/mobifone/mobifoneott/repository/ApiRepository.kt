package com.example.webrtcdemoandroid.repository

import com.mobifone.mobifoneott.network.token.TokenResponse
import com.mobifone.mobifoneott.service.ApiService


interface ApiRepository {
    suspend fun getToken(name: String): TokenResponse
}


class ApiRepositoryImpl: ApiRepository {
    private val apiService = ApiService.create()
    override suspend fun getToken(name: String): TokenResponse {
        return apiService.getToken(name)
    }
}