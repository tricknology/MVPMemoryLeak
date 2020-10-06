package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.util.Log
import com.drawingboardapps.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.InteractorLeaksCallback
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PresenterLeaksViaCapturingLambdaRX(
    val view: ViewContract,
    private val schedulerProvider: SchedulerProvider = SchedulerProvider(),
    private val interactor: LeakInteractor = InteractorLeaksCallback()
) : BasePresenter() {
    private val disposable = CompositeDisposable()

    override fun causeLeak() {
        Log.d(TAG, "causeLeak: ")
        disposable.add(
            doWorkOnAnotherThread()
                .observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.ui())
                .subscribe()
        )
        view.changeView(FragmentType.Leak)
    }
    private fun doWorkOnAnotherThread(): Single<Unit> {
        Log.d(TAG, "doWorkOnAnotherThread: ")

        return Single.fromCallable {
            Log.d(TAG, "doWorkOnAnotherThread: calling interactor.execute()")
            interactor(
                onSuccess = {
                    Log.d(TAG, "onSuccess: ")
                    val state = ViewState.Update(it.result)
                    view.updateViewState(state)
                },
                onError = {
                    Log.d(TAG, "onError: ")
                    displayError(it)
                }
            )
        }.also { Log.d(TAG, "returning single: ") }
    }


    private fun displayError(failureResult: Result.Fail) {
        Log.d(TAG, "displayError: ")
        val error = getFailureResutMessage(failureResult)
        view.updateViewState(ViewState.Update(error))
    }

    private fun getFailureResutMessage(failureResult: Result.Fail): String {
        return failureResult.exception.message.toString()
    }

    override fun avoidLeak() {
        Log.d(TAG, "avoidLeak: disposing $disposable")

        if (!disposable.isDisposed) {
            disposable.clear()
        }
    }

    override fun destroy() {
        Log.d(TAG, "destroy: ")
        avoidLeak()
    }

    companion object {
        val TAG = PresenterLeaksViaCapturingLambdaRX::class.simpleName
    }
}
