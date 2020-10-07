package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType

class InteractorFactory {
    fun getInteractor(leakType: LeakType) : LeakInteractor {
        return when(leakType){
            LeakType.ViaCallback -> InteractorLeaksCallback()
            LeakType.ViaRX -> InteractorLeaksDisposable()
            LeakType.ViaRunnable -> InteractorLeaksRunnable()
        }
    }
}