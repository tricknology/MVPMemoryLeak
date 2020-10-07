package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.LeakInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.SingleInteractor


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




