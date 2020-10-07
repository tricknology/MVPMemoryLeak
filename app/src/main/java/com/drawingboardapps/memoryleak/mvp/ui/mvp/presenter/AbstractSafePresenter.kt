package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.SRPRxLiveInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

abstract class AbstractSafePresenter(
    private var viewContract: ViewContract?,
    private var interactor: LeakInteractor? = SRPRxLiveInteractor(),
    private val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : BasePresenter() {

    private var resultListener = fun(result: Result) {
        val outcome = determineViewState(result)
        viewContract?.updateViewState(outcome)
    }

    private var errorHandler = fun(t: Throwable) {
        viewContract?.updateViewState(determineViewState(Result.Fail(t)))
    }

    private fun determineViewState(result: Result): ViewState {
        return when (result) {
            is Result.Success -> ViewState.Update(result.result)
            is Result.Fail -> ViewState.Update(getExceptionMessage(result))
        }
    }

    private fun getExceptionMessage(it: Result.Fail): String {
        return it.exception.message ?: it.exception.toString()
    }

    override fun causeLeak() {
        interactor?.let {
            when (it) {
                is SRPRxLiveInteractor -> doRxWork(it)
                else -> it.invoke(resultListener, resultListener)
            }
        }

    }

    /**
     * Perform work using [SRPRxLiveInteractor].
     * Subscribes on the ui thread and observes on io thread provided by [schedulerProvider]
     *
     */
    private fun doRxWork(interactor: SRPRxLiveInteractor) {
        interactor()
            .observeOn(schedulerProvider.io())
            .subscribeOn(schedulerProvider.ui())
            .subscribe(resultListener, errorHandler)
            ?.also {
                compositeDisposable.add(it)
            }
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.clear()
        viewContract = null
        interactor?.destroy()
        interactor = null
    }
}