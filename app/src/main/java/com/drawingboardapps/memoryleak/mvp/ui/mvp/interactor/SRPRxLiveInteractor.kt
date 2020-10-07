package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
import com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor.SRPInteractor
import io.reactivex.rxjava3.core.Single

/**
 * Wraps the call in Single.fromCallable
 */
class SRPRxLiveInteractor : SRPInteractor() {

    override fun invoke(): Single<Result> = Single.fromCallable { result }

}
