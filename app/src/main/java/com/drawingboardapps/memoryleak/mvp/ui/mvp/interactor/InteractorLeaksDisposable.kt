package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import android.util.Log
import com.drawingboardapps.memoryleak.core.rx.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
/**
 * [execute] leaks the implementation through an anonymous thread
 */
class InteractorLeaksDisposable(val scheduler: SchedulerProvider = SchedulerProvider()) : LeakInteractor {

    private val compositeDisposable = CompositeDisposable()

    operator fun invoke() {
        val single = wrapWorkInSingle()
        val disposable = single
            .observeOn(scheduler.io())
            .subscribeOn(scheduler.ui())
            .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun wrapWorkInSingle(): Single<Result> {
        var result: Result? = null
        return Single.fromCallable {
            val onSuccess: ((Result.Success) -> Unit) = {
                result = Result.Success("Success")
            }
            val onError: ((Result.Fail) -> Unit) = {
                result = Result.Fail(Throwable("Failed"))
            }
            invoke(onSuccess, onError)
            result
        }
    }

    override fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    ) {
        Log.d(TAG, "execute: starting thread")
        Thread {
            Log.d(TAG, "Executing Threaded work: ")
            Thread.sleep(10000)
            Log.d(TAG, "Finished Executing Threaded work, calling back onSuccess ")
            //holding a reference to onSuccess which is implemented who knows where
            onSuccess(
                Result.Success(
                    "Success"
                )
            )
        }.start()
        Log.d(TAG, "execute: started thread ")
    }

    override fun destroy() {
        Log.d(TAG, "destroy: called")
        compositeDisposable.clear()
    }

    companion object {
        val TAG: String = InteractorLeaksDisposable::class.java.simpleName
    }
}


