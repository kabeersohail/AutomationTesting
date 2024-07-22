package com.wesupport.automationtesting

import android.os.Build
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var decorView: View

    @Before
    fun setUp() {
        activityRule.scenario.onActivity { activity ->
            decorView = activity.window.decorView
        }
    }

    @Test
    fun loginWithCorrectCredentials() {
        // Type username
        onView(withId(R.id.usernameEditText))
            .perform(typeText("user"), closeSoftKeyboard())

        // Type password
        onView(withId(R.id.passwordEditText))
            .perform(typeText("pass"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the success message is displayed in a Toast
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            onView(withText("Login successful"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
        } else {
            onView(withText("Login successful")).inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
        }
    }

    fun isToastMessageDisplayed(textId: Int) {
        onView(withText(textId)).inRoot(isToast()).check(matches(isDisplayed()))
    }

    private fun isToast(): Matcher<Root?> {
        return ToastMatcher()
    }

    @Test
    fun loginWithIncorrectCredentials() {
        // Type incorrect username
        onView(withId(R.id.usernameEditText))
            .perform(typeText("wronguser"), closeSoftKeyboard())

        // Type incorrect password
        onView(withId(R.id.passwordEditText))
            .perform(typeText("wrongpass"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.loginButton)).perform(click())

        // Check if the failure message is displayed in a Toast
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            onView(withText("Login failed"))
                .inRoot(withDecorView(not(decorView)))
                .check(matches(isDisplayed()))
        } else {
            onView(withText("Login failed")).inRoot(ToastMatcher().apply {
                matches(isDisplayed())
            })
        }
    }
}