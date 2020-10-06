package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import android.util.Log.*
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState

class PresenterLeaksViaCapturingRunnable(
    private val view: ViewContract,
    private val interactor: LeakInteractor
) : BasePresenter() {

    companion object {
        val TAG: String = PresenterLeaksViaCapturingRunnable::class.java.simpleName
    }

    override fun causeLeak() {
        d(TAG, "causeLeak:")
        when (interactor) {
            is InteractorLeaksRunnable -> invokeRunnable(interactor)
            is InteractorLeaksDisposable -> invokeDisposable(interactor)
            is InteractorLeaksCallback -> invokeCallback(interactor)
        }
        d(TAG, "leaving early:")
        view.changeView(FragmentType.Leak)
    }

    private fun invokeCallback(interactor: InteractorLeaksCallback) {
        d(TAG, "invokeCallback:")
        interactor(::handleSuccess, ::handleError)
    }

    private fun invokeDisposable(interactor: InteractorLeaksDisposable) {
        d(TAG, "invokeDisposable:")
        interactor(::handleSuccess, ::handleError)
    }

    private fun handleError(it: Result.Fail) {
        d(TAG, "onError:")
        view.updateViewState(ViewState.Update("Exception ${it.exception.message.toString()}"))
    }

    private fun handleSuccess(it: Result.Success) {
        d(TAG, "onSuccess:")
        view.updateViewState(ViewState.Update(it.result))
    }

    private fun invokeRunnable(interactor: InteractorLeaksRunnable) {
        d(TAG, "invokeRunnable: ")
        interactor.invoke(Runnable {
            d(TAG, "Executing Threaded work: ")
            Thread.sleep(20000)
            d(TAG, "Finished Executing Threaded work, calling back onSuccess ")
            //holding a reference to onSuccess which is implemented who knows where
            handleSuccess(Result.Success("Success"))
        })
    }

    override fun destroy() {
        //no can do
        interactor.destroy()
    }

    override fun avoidLeak() {
        // nope
    }

}