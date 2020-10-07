package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
/**
 * [PresenterLeaksViaCapturingRunnable] is a leaky presenter because it has a strong reference to
 * view and the interactor.  In a sense it binds the two until all processes are either complete or
 * no longer hold a reference to the presenter, or view.
 */
class PresenterLeaksViaCapturingRunnable(
    private val view: ViewContract,
    private val interactor: LeakInteractor
) : BasePresenter() {


    /**
     * Try to cause a leak here.
     */
    override fun causeLeak() {
        interactor.invoke(::handleSuccess, ::handleError)
        //as lambda
        // causeLeakUsingLambda()
    }

    /**
     * Try to avoid a leak here by calling destroy on the interactor
     */
    override fun avoidLeak() {
        // maybe interactor will obey, maybe not
        interactor.destroy()
    }

    /**
     * Try to avoid a leak
     */
    override fun destroy() {
        super.destroy()
        //no can do
        avoidLeak()
    }

    /**
     * [handleError] as implemented here captures view and will leak it until:
     * 1. work is finished in the [interactor]
     * 2. if implemented, [destroy] dereferences the callbacks
     */
    private fun handleError(it: Result.Fail) {
        d(TAG, "onError:")
        view.updateViewState(ViewState.Update("Exception ${it.exception.message.toString()}"))
    }

    /**
     * [handleSuccess] as implemented here captures view and will leak it until:
     * 1. work is finished in the [interactor]
     * 2. if implemented, [destroy] dereferences the callbacks
     */
    private fun handleSuccess(it: Result.Success) {
        d(TAG, "onSuccess:")
        view.updateViewState(ViewState.Update(it.result))
    }


    /**
     * Leak in [causeLeakUsingLambda] as the lambda function references [view] in the enclosing class.
     * As long as the callback is referenced, the view will be referenced too.
     */
    private fun causeLeakUsingLambda(){
        interactor({
            view.updateViewState(ViewState.Update(it.result))
        },{
            view.updateViewState(ViewState.Update("Exception ${it.exception.message.toString()}"))
        })
    }

    companion object {
        val TAG: String = PresenterLeaksViaCapturingRunnable::class.java.simpleName
    }

}