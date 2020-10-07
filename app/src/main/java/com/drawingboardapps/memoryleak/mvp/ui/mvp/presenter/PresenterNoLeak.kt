package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.SRPRxLiveInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * This presenter uses a nullable view to avoid leaks.
 *
 */
class PresenterNoLeak(
    var view: ViewContract?,
    var interactor: LeakInteractor? = SRPRxLiveInteractor(),
    private val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : BasePresenter() {

    private var disposables: CompositeDisposable? = CompositeDisposable()

    override fun destroy() {
        disposables?.clear()
        disposables = null
        interactor?.destroy()
        interactor = null
        view = null
    }

    override fun causeLeak() {

        doWorkOnAnotherThread()
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.ui())
            .doOnError {
                displayError(
                    Result.Fail(it)
                )
            }
            .subscribe()
        view!!.changeView(FragmentType.Leak)
    }

    override fun avoidLeak() {
        view!!.changeView(FragmentType.NoLeak)
    }

    private fun doWorkOnAnotherThread(): Single<Any> {
        return Single.fromCallable {
            interactor?.invoke(
                onSuccess = {
                    updateViewState(it)
                },
                onError = {
                    displayError(it)
                }
            )
        }
    }

    private fun updateViewState(it: Result.Success) {
        val state = ViewState.Update(it.result)
        view?.updateViewState(state)
    }

    private fun displayError(failureResult: Result.Fail) {
        val error = failureResult.exception.message.toString()
        view?.updateViewState(ViewState.Update(error))
    }
}
