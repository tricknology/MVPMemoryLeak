package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType
import leakcanary.FailTestOnLeak
import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentLeakTest {

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRX_and_PresenterType_Safe_FragmentA_Does_Not_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRX,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<SafeFragment>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaCallback_and_PresenterType_Safe_FragmentA_Does_Not_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaCallback,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<SafeFragment>(fragmentArgs)
    }
    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRunnable_and_PresenterType_Safe_FragmentA_Does_Not_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRunnable,
            PresenterType.Safe
        )
        //when
        executeTestOnFragment<SafeFragment>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRX_and_PresenterType_Immutable_FragmentB_Does_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRX,
            PresenterType.Unsafe
        )
        //when
        executeTestOnFragment<LeakyFragment>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaRunnable_and_PresenterType_Immutable_fragmentB_Does_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaRunnable,
            PresenterType.Unsafe
        )
        //when
        executeTestOnFragment<LeakyFragment>(fragmentArgs)
    }

    @Test
    @FailTestOnLeak
    fun given_LeakType_ViaCallback_and_PresenterType_Immutable_FragmentB_Does_Leak() {
        //given
        val fragmentArgs = getFragmentArgs(
            LeakType.ViaCallback,
            PresenterType.Unsafe
        )
        //when
        with(getFragmentScenario<LeakyFragment>(fragmentArgs)) {
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