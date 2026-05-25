package com.example.gofit

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testNavigationAndLogcatTriggers() {
        // 1. Cek Home
        onView(withId(R.id.nav_home)).check(matches(isDisplayed()))

        // 2. Ke Workout - Ini akan mentrigger Log "fetchWorkouts called"
        onView(withId(R.id.nav_workout)).perform(click())
        Thread.sleep(1000) // Tunggu loading API sebentar
        onView(withId(R.id.recycler_workout)).check(matches(isDisplayed()))

        // 3. Ke Profile - Cek apakah data profil muncul
        onView(withId(R.id.nav_profile)).perform(click())
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_edit_profile)).check(matches(isDisplayed()))
    }
}
