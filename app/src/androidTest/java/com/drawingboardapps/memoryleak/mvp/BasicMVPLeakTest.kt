package com.drawingboardapps.memoryleak.mvp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.activity.MVPLeakActivity
import leakcanary.FailTestOnLeak

import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

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