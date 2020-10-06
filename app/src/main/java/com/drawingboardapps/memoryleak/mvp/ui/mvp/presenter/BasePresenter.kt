package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState
import io.reactivex.rxjava3.disposables.CompositeDisposable

interface Presenter :
    LeakContract {
    fun destroy()
}

abstract class BasePresenter : Presenter {
    val compositeDisposable = CompositeDisposable()

    override fun destroy() {
        compositeDisposable.dispose()
    }

}

abstract class SafeBasePresenter(
    private var viewContract: ViewContract?,
    private var interactor: SRPRxLiveInteractor? = SRPRxLiveInteractor(),
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
            it()
                .observeOn(schedulerProvider.io())
                .subscribeOn(schedulerProvider.ui())
                .subscribe(resultListener, errorHandler)
        }?.also {
            compositeDisposable.add (it)
        }
    }

    override fun destroy() {
        super.destroy()
        compositeDisposable.clear()
    }
}
