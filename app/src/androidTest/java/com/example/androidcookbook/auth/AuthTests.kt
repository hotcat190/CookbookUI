package com.example.androidcookbook.auth

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.example.androidcookbook.MainActivity
import com.example.androidcookbook.auth.TestAuthCredentials.password
import com.example.androidcookbook.auth.TestAuthCredentials.username
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.ui.features.auth.components.PASSWORD_TEXT_FIELD_TEST_TAG
import com.example.androidcookbook.ui.features.auth.components.USERNAME_TEXT_FIELD_TEST_TAG
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class AuthTests {
    @Inject
    lateinit var authRepository: AuthRepository

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.androidcookbook", appContext.packageName)
    }

    @Test
    fun testLogin() {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(USERNAME_TEXT_FIELD_TEST_TAG).performTextInput(username)
        composeTestRule.onNodeWithTag(PASSWORD_TEXT_FIELD_TEST_TAG).performTextInput(password)
        composeTestRule.onNodeWithText("Sign In").performClick()
    }
}