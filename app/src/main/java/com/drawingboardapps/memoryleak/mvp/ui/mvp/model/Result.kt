package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

sealed class Result {
    data class Success(val result: String) : Result()
    data class Fail(val exception: Throwable) : Result()
}
