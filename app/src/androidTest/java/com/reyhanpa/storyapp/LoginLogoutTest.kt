package com.reyhanpa.storyapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.reyhanpa.storyapp.ui.auth.LoginActivity
import com.reyhanpa.storyapp.utils.IdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginLogoutTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        androidx.test.espresso.IdlingRegistry.getInstance().register(IdlingResource.countingIdlingResource)
    }

    @After
    fun teardown() {
        androidx.test.espresso.IdlingRegistry.getInstance().unregister(IdlingResource.countingIdlingResource)
    }

    @Test
    fun testLoginLogoutFlow() {
        onView(withId(R.id.ed_login_email))
            .perform(typeText("reyhangans@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.ed_login_password))
            .perform(typeText("reyhan123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton))
            .perform(click())

        onView(withId(R.id.main))
            .check(matches(isDisplayed()))

        onView(withId(R.id.action_logout))
            .perform(click())

        onView(withId(R.id.welcome))
            .check(matches(isDisplayed()))
    }
}