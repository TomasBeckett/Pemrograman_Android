package com.example.gofit.data.remote

import com.google.gson.annotations.SerializedName

data class MuscleResponse(
    @SerializedName("results")
    val results: List<Muscle>
)

data class Muscle(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
