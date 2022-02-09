package com.example.webrtcdemoandroid.constants

object ApiConstants {
    const val timeout = 20L
    val baseUrl: String by lazy { baseUrl() }

    private fun baseUrl(): String {
        return "http://ott.mobifone.ai/"
    }
}