package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.runner.AndroidJUnitRunner
import com.drawingboardapps.memoryleak.mvp.BundleArgs
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.FragmentBase
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterType
import leakcanary.FailTestOnLeak
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentLeakTest {

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRX_and_PresenterType_Safe_FragmentA_DoesNotLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRX,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<FragmentA>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaCallback_and_PresenterType_Safe_FragmentA_DoesNotLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaCallback,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<FragmentA>(fragmentArgs)
    }
    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRunnable_and_PresenterType_Safe_FragmentA_DoesNotLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRunnable,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<FragmentA>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRX_and_PresenterType_Nullable_FragmentB_DoesLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRX,
            PresenterType.Nullable
        )
        //when
        executeTestOnFragment<FragmentB>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRunnable_and_PresenterType_Nullable_fragmentBDoesLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRunnable,
            PresenterType.Nullable
        )
        //when
        executeTestOnFragment<FragmentB>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaCallback_and_PresenterType_Nullable_FragmentB_DoesLeak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaCallback,
            PresenterType.Nullable
        )
        //when
        with(getFragmentScenario<FragmentB>(fragmentArgs)) {
            Log.d("FragmentFactoryTest", "pressing button")
            onFragment { it.presenter?.causeLeak() }
        }
    }

    private fun getFragmentArgs(
        leakType: LeakType,
        presenterType: PresenterType
    ): Bundle = Bundle().apply {
        putParcelable(BundleArgs.LEAK_TYPE, leakType)
        putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
    }

}

inline fun <reified F : FragmentBase> executeTestOnFragment(fragmentArgs: Bundle) {
    with(getFragmentScenario<F>(fragmentArgs)) {
        Log.d("FragmentFactoryTest", "pressing button")
        onFragment { it.presenter?.causeLeak() }
    }
}

inline fun <reified F : Fragment> getFragmentScenario(fragmentArgs: Bundle): FragmentScenario<F> {
    Log.d("FragmentFactoryTest", "getFragmentScenario")
    return launchFragmentInContainer(
        fragmentArgs = fragmentArgs,
        themeResId = R.style.Theme_AppCompat
    )
}