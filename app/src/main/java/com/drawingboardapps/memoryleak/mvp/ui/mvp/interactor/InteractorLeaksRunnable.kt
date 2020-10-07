package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import android.util.Log
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result
/**
 * [execute] leaks the implementation through an anonymous thread
 */
class InteractorLeaksRunnable : LeakInteractor {

    operator fun invoke(runnable: Runnable) {
        invoke(
            onSuccess = {runnable.run()},
            onError = {runnable.run()}
        )
    }

    override fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    ) {
        val runnable = Runnable {
            Log.d(TAG, "Executing Threaded work: ")
            Thread.sleep(10000)
            Log.d(TAG, "Finished Executing Threaded work, calling back onSuccess ")
            //holding a reference to onSuccess which is implemented who knows where
            onSuccess(Result.Success("Success"))
        }
        Log.d(TAG, "execute: starting thread")
        Thread(runnable).start()
        Log.d(TAG, "execute: started thread ")
    }

    override fun destroy() {
        Log.d(TAG, "destroy: destroy() unimplemented")
    }

    companion object {
        val TAG: String = InteractorLeaksRunnable::class.java.simpleName
    }
}


