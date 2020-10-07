package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import androidx.fragment.app.Fragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType

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
