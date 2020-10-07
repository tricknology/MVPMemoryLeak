package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import android.util.Log
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

/**
 * [invoke] leaks the implementation of onSuccess and onError via [thread] since it holds a
 * strong reference to the callbacks.
 *
 */
class InteractorLeaksCallback : LeakInteractor {

    private lateinit var thread: Thread
    /**
     * [invoke] runs the work simulated in [doWork] on [thread]
     */
    override fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    ) {
        Log.d(TAG, "invoke: ")
        // local runnable instance that calls directly to onSuccess
        thread = Thread(doWork(onSuccess)).also { startThread(it, onError) }
    }

    private fun startThread(it: Thread, onError: ((Result.Fail) -> Unit)) {
        try {
            it.start()
            Log.d(TAG, " started $it} ")
        } catch (ex: InterruptedException) {
            if (Thread.interrupted()) {
                onError(Result.Fail(ex))
            }
        }
    }

    /**
     * [doWork] returns a runnable that simulates work.
     */
    private fun doWork(onSuccess: ((Result.Success) -> Unit)): Runnable = Runnable {
        Log.d(TAG, "Executing Threaded work: ")
        Thread.sleep(10000)
        Log.d(TAG, "Finished Executing Threaded work, calling back onSuccess ")
        //holding a reference to onSuccess which is implemented who knows where
        val result = Result.Success("Success")
        onSuccess(result)
    }

    override fun destroy() {
        Log.d(TAG, "destroy: destroy() unimplemented for demo purposes")
    }

    companion object {
        val TAG: String = InteractorLeaksCallback::class.java.simpleName
    }
}


