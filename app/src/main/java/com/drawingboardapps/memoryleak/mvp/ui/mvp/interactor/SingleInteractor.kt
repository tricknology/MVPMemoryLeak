package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import io.reactivex.rxjava3.core.Single

interface SingleInteractor<out T> {
    operator fun invoke(): Single<out T>
}