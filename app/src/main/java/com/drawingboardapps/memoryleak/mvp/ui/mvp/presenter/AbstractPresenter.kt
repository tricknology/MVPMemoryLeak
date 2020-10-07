package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksCallback
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksDisposable
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksRunnable
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

abstract class AbstractPresenter(var view: ViewContract?, var interactor: LeakInteractor?) : BasePresenter() {

    abstract fun handleSuccess(result: Result.Success)

    abstract fun handleError(result: Result.Fail)

    override fun causeLeak() {
        Log.d(TAG, "causeLeak:")
        interactor?.let {
            when (it) {
                is InteractorLeaksRunnable -> {
                    invokeRunnable(it)
                }
                is InteractorLeaksDisposable -> invokeDisposable(it)
                is InteractorLeaksCallback -> invokeCallback(it)
            }
        }
        Log.d(TAG, "leaving early:")
        view?.changeView(FragmentType.Leak)
    }

    private fun invokeCallback(interactor: InteractorLeaksCallback) {
        Log.d(TAG, "invokeCallback:")
        interactor(::handleSuccess, ::handleError)
    }

    private fun invokeDisposable(interactor: InteractorLeaksDisposable) {
        Log.d(TAG, "invokeDisposable:")
        interactor(::handleSuccess, ::handleError)
    }

    private fun invokeRunnable(interactor: InteractorLeaksRunnable) {
        Log.d(PresenterLeaksViaCapturingRunnable.TAG, "invokeRunnable: ")
        interactor.invoke(Runnable {
            Log.d(TAG, "Executing Threaded work: ")
            Thread.sleep(20000)
            Log.d(TAG, "Finished Executing Threaded work, calling back onSuccess ")
            //holding a reference to onSuccess which is implemented who knows where
            handleSuccess(Result.Success("Success"))
        })
    }
    companion object {
        val TAG : String = AbstractPresenter::class.simpleName!!
    }

}
