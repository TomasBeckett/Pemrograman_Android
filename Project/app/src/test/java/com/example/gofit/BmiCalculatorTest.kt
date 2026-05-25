package com.example.gofit

import com.example.gofit.utils.BmiCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class BmiCalculatorTest {

    @Test
    fun calculateBmi_isCorrect() {
        val weight = 70
        val height = 175
        val expected = 22.857
        val result = BmiCalculator.calculateBmi(weight, height)
        assertEquals(expected, result, 0.001)
    }

    @Test
    fun getBmiStatus_isCorrect() {
        assertEquals("Underweight", BmiCalculator.getBmiStatus(17.0))
        assertEquals("Ideal Weight", BmiCalculator.getBmiStatus(22.0))
        assertEquals("Overweight", BmiCalculator.getBmiStatus(27.0))
        assertEquals("Obese", BmiCalculator.getBmiStatus(32.0))
    }

    @Test
    fun calculateBmi_withZero_returnsZero() {
        assertEquals(0.0, BmiCalculator.calculateBmi(0, 175), 0.0)
        assertEquals(0.0, BmiCalculator.calculateBmi(70, 0), 0.0)
    }
}
