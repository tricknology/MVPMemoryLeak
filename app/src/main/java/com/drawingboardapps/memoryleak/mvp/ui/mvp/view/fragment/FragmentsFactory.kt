package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import androidx.fragment.app.Fragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType

object FragmentsFactory {
    fun getFragment(
        fragmentType: FragmentType,
        presenterType: PresenterType,
        leakType: LeakType,
        buttonPressCount: Int
    ): Fragment {

        return when (fragmentType) {
            FragmentType.NoLeak -> {
                SafeFragment.newInstance(leakType, presenterType, buttonPressCount)
            }
            FragmentType.Leak -> {
                LeakyFragment.newInstance(leakType, presenterType, buttonPressCount)
            }
            FragmentType.Chooser -> {
                LeakChooserFragment.newInstance()
            }
        }
    }
}
