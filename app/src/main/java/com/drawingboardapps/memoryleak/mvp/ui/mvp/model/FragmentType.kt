package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.SafeFragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.LeakyFragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.LeakChooserFragment

sealed class FragmentType(open val tag : String){
    object NoLeak : FragmentType(
        SafeFragment.TAG
    )
    object Leak: FragmentType(
        LeakyFragment.TAG
    )
    object Chooser: FragmentType(
        LeakChooserFragment.TAG
    )
}