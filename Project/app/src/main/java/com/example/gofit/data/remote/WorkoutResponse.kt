package com.example.gofit.data.remote

import com.google.gson.annotations.SerializedName

data class WorkoutResponse(
    @SerializedName("results")
    val results: List<RemoteWorkout>?
)

data class RemoteWorkout(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?
)
