package com.drawingboardapps.memoryleak.mvp.ui.mvp.view

import com.drawingboardapps.memoryleak.core.FragmentRouter

interface MVPLeakActivityView : LeakContract {
    fun getFragmentRouter() : FragmentRouter
}