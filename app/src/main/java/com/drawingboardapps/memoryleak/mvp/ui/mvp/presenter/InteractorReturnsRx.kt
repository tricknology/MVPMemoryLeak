package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.Result
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.operators.single.SingleResumeNext
import java.util.concurrent.Callable

interface SingleInteractor<out T> {
    operator fun invoke(): Single<out T>
}

abstract class BaseSingleInteractor<out T> : SingleInteractor<T>, LeakInteractor {

    protected fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    override fun invoke(
        onSuccess: (Result.Success) -> Unit,
        onError: (Result.Fail) -> Unit
    ) {
        when (val result = doWork()) {
            is Result.Success -> onSuccess(result)
            is Result.Fail -> onError(result)
        }
    }

    fun doWork(): Result? {
        Thread.sleep(10000)
        return Result.Success("Success: ${getCurrentTime()}")
    }

}

abstract class SRPInteractor : BaseSingleInteractor<Result>() {

    protected var result: Result? = null
        get() {
            invoke(
                fun(success: Result.Success) { result = success },
                fun(error: Result.Fail) { result = error }
            )
            return field ?: Result.Fail(Throwable("Cancelled"))
        }

    override fun destroy() {
        result = null
    }
}

/**
 * Wraps the call in Single.fromCallable
 */
class SRPRxLiveInteractor : SRPInteractor() {

    override fun invoke(): Single<Result> = Single.fromCallable { result }

}


/**
 * Wraps the call in Single.just
 * This has the effect of executing
 */
class SRPRxDeadInteractor : SRPInteractor() {
    override fun invoke(): Single<Result> = Single.just(result)
}