package com.example.gofit.utils

object BmiCalculator {
    fun calculateBmi(weight: Int, height: Int): Double {
        if (weight <= 0 || height <= 0) return 0.0
        val heightInMeter = height / 100.0
        return weight / (heightInMeter * heightInMeter)
    }

    fun getBmiStatus(bmi: Double): String {
        return when {
            bmi <= 0 -> "N/A"
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Ideal Weight"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }
}
