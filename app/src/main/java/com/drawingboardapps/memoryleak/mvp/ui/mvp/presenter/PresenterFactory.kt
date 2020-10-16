package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import com.drawingboardapps.memoryleak.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorFactory
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType

object PresenterFactory {
    fun getPresenter(
        view: ViewContract,
        leakType: LeakType,
        presenterType: PresenterType
    ): BasePresenter {
        val interactor = InteractorFactory().getInteractor(leakType)
        return when (presenterType) {
            is PresenterType.Nullable -> getSafePresenter(view, interactor)
            is PresenterType.NonNullable -> getLeakyPresenter(view, leakType, interactor)
            is PresenterType.Safe -> getSafePresenter(view, interactor)
        }.also {
            Log.d("PresenterFactory", "view: $view")
            Log.d("PresenterFactory", "leakType: $leakType")
            Log.d("PresenterFactory", "presenterType: $presenterType")
            Log.d("PresenterFactory", "returning: $it")
        }
    }

    private fun getLeakyPresenter(
        view: ViewContract,
        leakType: LeakType,
        interactor: LeakInteractor
    ): BasePresenter = when (leakType) {
        is LeakType.ViaRX -> PresenterLeaksViaCapturingLambdaRX(
            view,
            interactor,
            SchedulerProvider()
        )
        is LeakType.ViaRunnable -> PresenterLeaksViaCapturingRunnable(view, interactor)
        is LeakType.ViaCallback -> PresenterLeaksViaCapturingCallback(view, interactor)
    }


    private fun getSafePresenter(
        view: ViewContract,
        interactor: LeakInteractor
    ): BasePresenter = PresenterNoLeak(
        view,
        interactor,
        SchedulerProvider()
    )

}