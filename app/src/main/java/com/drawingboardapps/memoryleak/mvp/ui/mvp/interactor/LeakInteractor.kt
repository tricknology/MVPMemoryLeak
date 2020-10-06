package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

sealed class Result {
    data class Success(val result: String) : Result()
    data class Fail(val exception: Throwable) : Result()
}

interface LeakInteractor {

    operator fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    )

    fun destroy()
}

