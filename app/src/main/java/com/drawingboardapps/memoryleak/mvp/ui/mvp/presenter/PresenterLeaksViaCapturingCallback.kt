package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksCallback
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState

/**
 * [PresenterLeaksViaCapturingCallback] is a leaky presenter because it has a strong reference to
 * view and the interactor.  In a sense it binds the two until all processes are either complete or
 * no longer hold a reference to the presenter, or view.
 */
class PresenterLeaksViaCapturingCallback(
    val view: ViewContract,
    val interactor : LeakInteractor = InteractorLeaksCallback()
) : BasePresenter() {

    override fun causeLeak() {
        Log.d(TAG, "causeLeak: ")
        doWorkOnAnotherThread()
        view.changeView(FragmentType.Leak)
    }

    /**
     * Does some work on another thread as dictated by the interactor.
     * onSuccess and onError are implemented here as anonymous lambda functions that capture
     * the enclosing class via [displayError] and [displaySuccess]
     */
    private fun doWorkOnAnotherThread() {
        Log.d(TAG, "doWorkOnAnotherThread: calling interactor.invoke()")

        return interactor(
            onSuccess = {
                Log.d(TAG, "onSuccess: ")
                displaySuccess(it)
            },
            onError = {
                Log.d(TAG, "onError: ")
                displayError(it)
            }
        ).also { Log.d(TAG, "returning single: ") }
    }

    /**
     * Calls the view to update view state based on [Result.Success]
     */
    private fun displaySuccess(it : Result.Success) {
        val state = ViewState.Update(it.result)
        view.updateViewState(state)
    }

    /**
     * Calls the view to update view state based on [Result.Fail]
     */
    private fun displayError(failureResult: Result.Fail) {
        Log.d(TAG, "displayError: ")
        val error = getFailureResutMessage(failureResult)
        view.updateViewState(ViewState.Update(error))
    }

    //TODO inline function
    private fun getFailureResutMessage(failureResult: Result.Fail): String {
        return failureResult.exception.message.toString()
    }

    override fun avoidLeak() {
        Log.d(TAG, "avoidLeak:")
    }

    override fun destroy() {
        Log.d(TAG, "destroy: ")
        //calls compositeDisposable.clear()
        super.destroy()
        avoidLeak()
    }

    companion object {
        val TAG = PresenterLeaksViaCapturingCallback::class.simpleName
    }
}
