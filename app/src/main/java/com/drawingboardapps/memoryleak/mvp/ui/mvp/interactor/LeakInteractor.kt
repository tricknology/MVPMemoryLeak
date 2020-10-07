package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

interface LeakInteractor {

    operator fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    )

    fun destroy()
}



