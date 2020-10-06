package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.os.Parcelable
import android.util.Log
import com.drawingboardapps.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorFactory
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import kotlinx.android.parcel.Parcelize

sealed class PresenterType : Parcelable {
    /**
     * Presenter implements a nullable view that is de-referenced when destroy() is called
     */
    @Parcelize
    object Nullable : PresenterType()
    /**
     * Presenter implements a non-nullable view that is not de-referenced when destroy() is called
     */
    @Parcelize
    object NonNullable : PresenterType()
    /**
     * Presenter implements various safety precautions to ensure that view that is de-referenced
     * when destroy() is called
     */
    @Parcelize
    object Safe : PresenterType()
}

sealed class LeakType : Parcelable {
    @Parcelize
    object ViaRunnable : LeakType()

    @Parcelize
    object ViaCallback : LeakType()

    @Parcelize
    object ViaRX : LeakType()
//    @Parcelize
//    object NoLeak : LeakType()
}

object PresenterFactory {
    fun getPresenter(
        view: ViewContract,
        leakType: LeakType,
        presenterType: PresenterType
    ): BasePresenter{
        val interactor = InteractorFactory().getInteractor(leakType)
        return when (presenterType) {
            is PresenterType.Nullable -> getNullablePresenter(view, leakType, interactor)
            is PresenterType.NonNullable -> getNonNullablePresenter(view, leakType, interactor)
            is PresenterType.Safe -> getSafePresenter(view,interactor)
        }.also {
            Log.d("PresenterFactory", "view: $view")
            Log.d("PresenterFactory", "leakType: $leakType")
            Log.d("PresenterFactory", "presenterType: $presenterType")
            Log.d("PresenterFactory", "returning: $it")
        }
    }

    private fun getSafePresenter(
        view: ViewContract,
        interactor: LeakInteractor
    ): BasePresenter = PresenterNoLeak(
            view,
            interactor,
            SchedulerProvider()
        )

    private fun getNullablePresenter(
        view: ViewContract,
        leakType: LeakType,
        interactor: LeakInteractor
    ): BasePresenter =
        when (leakType) {
            is LeakType.ViaRX -> PresenterLeaksViaCapturingLambdaRX(view, SchedulerProvider(), interactor)
            is LeakType.ViaRunnable -> PresenterLeaksViaCapturingRunnable(view, interactor)
            LeakType.ViaCallback -> PresenterLeaksViaCapturingCallback(view, interactor)
        }

    private fun getNonNullablePresenter(
        view: ViewContract,
        leakType: LeakType,
        interactor: LeakInteractor
    ): BasePresenter = when (leakType) {
        is LeakType.ViaRX -> PresenterLeaksViaCapturingLambdaRX(
            view,
            SchedulerProvider(),
            interactor
        )
        is LeakType.ViaRunnable -> PresenterLeaksViaCapturingRunnable(view, interactor)
        LeakType.ViaCallback -> PresenterLeaksViaCapturingCallback(view, interactor)
    }
}