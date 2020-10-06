package com.drawingboardapps.memoryleak.mvp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.activity.MVPLeakActivity
import kotlinx.android.synthetic.main.fragment_basic_a.*
import leakcanary.FailTestOnLeak

import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class BasicMVPLeakTest {

    @get:Rule
    var activityRule = ActivityTestRule<MVPLeakActivity>(
        MVPLeakActivity::class.java)

    @Test
    @FailTestOnLeak
    fun buttonOpensFragment() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.drawingboardapps.memoryleak.mvp.memoryleak", appContext.packageName)
//        Espresso.onView(withId(R.id.button_leak)).perform(click())
    }
}