package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterType

sealed class FragmentType(open val tag : String){
    object NoLeak : FragmentType(FragmentA.TAG)
    object Leak: FragmentType(FragmentB.TAG)
    object Chooser: FragmentType(LeakChooserFragment.TAG)
}

object FragmentsFactory {
    fun getFragment(
        fragmentType: FragmentType,
        presenterType: PresenterType,
        leakType: LeakType
    ): Fragment {

        return when (fragmentType) {
            FragmentType.NoLeak -> {
                FragmentA.newInstance(leakType, presenterType)
            }
            FragmentType.Leak -> {
                FragmentB.newInstance(leakType, presenterType)
            }
            FragmentType.Chooser -> {
                LeakChooserFragment.newInstance()
            }
        }
    }
}
