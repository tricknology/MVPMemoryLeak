package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.BaseSingleInteractor
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

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
