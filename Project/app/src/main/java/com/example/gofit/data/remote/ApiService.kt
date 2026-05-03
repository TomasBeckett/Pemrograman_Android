package com.example.gofit.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("exercise/?language=2")
    suspend fun getExercises(): WorkoutResponse

    @GET("muscle/")
    suspend fun getMuscles(): MuscleResponse

    companion object {
        private const val BASE_URL = "https://wger.de/api/v2/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
