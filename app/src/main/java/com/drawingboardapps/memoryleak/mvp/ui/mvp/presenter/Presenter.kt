package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakContract

interface Presenter : LeakContract {
    fun destroy()
}
