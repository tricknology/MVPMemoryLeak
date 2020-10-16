package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import com.drawingboardapps.memoryleak.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksCallback
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState
import io.reactivex.rxjava3.core.Single

/**
 * [PresenterLeaksViaCapturingLambdaRX] is a leaky presenter because it has a strong reference to
 * view and the interactor.
 * <br>
 * This presenter has an RX component to it.
 * work is wrapped in a [Single] and allowed to execute on the io thread of the [schedulerProvider].
 * When the work is finished it notifies the observer on the ui thread of the [schedulerProvider].
 * When the [Single] is subscribed to, its [Disposable] is added to [compositeDisposable] which is
 * cleared when [PresenterLeaksViaCapturingLambdaRX.destroy] is called.
 * <br>
 *     Since onSuccess and onError callbacks are anonymous lambda functions which reference the
 *     view and methods of the presenter, even though the subscription is disposed, the Disposable
 *     still contains a final reference to the delegate.
 *
 */
@Suppress("KDocUnresolvedReference")
class PresenterLeaksViaCapturingLambdaRX(
    private val view: ViewContract,
    private val interactor: LeakInteractor = InteractorLeaksCallback(),
    private val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : BasePresenter() {

    /**
     * Cause a leak using RX, subscribing on ui, observing on io.
     * Changes the view (artificially) after performing some work.
     */
    override fun causeLeak() {
        Log.d(TAG, "causeLeak: ")
        compositeDisposable.add(
            doWorkOnAnotherThread()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .doOnError { displayError(Result.Fail(it)) }
                .subscribe { _ -> displaySuccess("onSuccess") }
        )
        view.changeView(FragmentType.Leak)
    }

    /**
     * returns a single that invokes [PresenterLeaksViaCapturingLambdaRX.interactor].
     * this interactor receives callbacks as parameters.
     * <br>
     * In this example,[PresenterLeaksViaCapturingLambdaRX.view] is accessed directly from the lambda
     * as well as [PresenterLeaksViaCapturingLambdaRX.displayError].
     * <br>
     * The interactor therefore has an implicit reference to the presenter
     * and its view and could potentially leak them.
     *
     */
    private fun doWorkOnAnotherThread(): Single<Unit> {
        Log.d(TAG, "doWorkOnAnotherThread: ")

        return Single.fromCallable {
            Log.d(TAG, "doWorkOnAnotherThread: calling interactor.execute()")
            interactor(
                onSuccess = {
                    Log.d(TAG, "onSuccess: ")
                    displaySuccess(it.result)
                },
                onError = {
                    Log.d(TAG, "onError: ")
                    displayError(it)
                }
            )
        }.also { Log.d(TAG, "returning single: ") }
    }

    private fun displaySuccess(msg : String) {
        view.updateViewState(ViewState.Update(msg) )
    }


    /**
     * Display an error message based on a [Result.Fail] message
     */
    private fun displayError(failureResult: Result.Fail) {
        Log.d(TAG, "displayError: ")
        val error = getFailureResultMessage(failureResult)
        view.updateViewState(ViewState.Update(error))
    }

    //TODO move to extension
    private fun getFailureResultMessage(failureResult: Result.Fail): String {
        return failureResult.exception.message.toString()
    }

    /**
     * This will call clear on the [compositeDisposable]
     */
    override fun avoidLeak() {
        Log.d(TAG, "avoidLeak:")
       super.destroy()
    }

    /**
     * This will call [avoidLeak]
     */
    override fun destroy() {
        Log.d(TAG, "destroy: ")
        avoidLeak()
    }

    companion object {
        val TAG = PresenterLeaksViaCapturingLambdaRX::class.simpleName
    }
}
