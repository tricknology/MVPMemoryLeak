package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.FragmentA
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.FragmentB
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.LeakChooserFragment

sealed class FragmentType(open val tag : String){
    object NoLeak : FragmentType(
        FragmentA.TAG
    )
    object Leak: FragmentType(
        FragmentB.TAG
    )
    object Chooser: FragmentType(
        LeakChooserFragment.TAG
    )
}