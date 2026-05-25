package com.example.gofit.data.remote

import com.google.gson.annotations.SerializedName

// API Ninjas returns a JSON Array directly, so we don't strictly need a wrapper class 
// if we use List<RemoteWorkout> in the ApiService.
// But we'll keep the file and define the RemoteWorkout here.

data class RemoteWorkout(
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("muscle")
    val muscle: String?,
    @SerializedName("equipment")
    val equipment: String?,
    @SerializedName("difficulty")
    val difficulty: String?,
    @SerializedName("instructions")
    val instructions: String?
)
