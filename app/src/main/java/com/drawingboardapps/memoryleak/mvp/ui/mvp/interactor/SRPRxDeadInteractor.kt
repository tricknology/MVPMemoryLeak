package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import io.reactivex.rxjava3.core.Single
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
/**
 * Wraps the call in Single.just
 * This has the effect of executing first and emitting later
 */
class SRPRxDeadInteractor : SRPInteractor() {
    override fun invoke(): Single<Result> = Single.just(result)
    override fun invoke(onSuccess: (Result.Success) -> Unit, onError: (Result.Fail) -> Unit) {}
}