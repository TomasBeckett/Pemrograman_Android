package com.example.gofit.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("exercises")
    suspend fun getExercises(
        @Header("X-Api-Key") apiKey: String,
        @Query("name") name: String? = null,
        @Query("type") type: String? = null,
        @Query("muscle") muscle: String? = null,
        @Query("difficulty") difficulty: String? = null
    ): List<RemoteWorkout>
}
