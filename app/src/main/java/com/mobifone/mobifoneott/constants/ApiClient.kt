package com.example.webrtcdemoandroid.constants

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object ApiClient {
    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(ApiConstants.timeout, TimeUnit.SECONDS)
            .build()
    }
}