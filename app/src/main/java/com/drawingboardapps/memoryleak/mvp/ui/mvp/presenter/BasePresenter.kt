package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BasePresenter : Presenter {
    val compositeDisposable = CompositeDisposable()

    override fun destroy() {
        compositeDisposable.dispose()
    }

}



