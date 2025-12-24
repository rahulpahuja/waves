package com.rahulpahuja.waves.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("example-endpoint")
    suspend fun getExampleData(): Any // Replace Any with actual data class
}
