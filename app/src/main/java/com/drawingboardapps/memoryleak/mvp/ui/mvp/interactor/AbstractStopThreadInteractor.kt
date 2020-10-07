package com.drawingboardapps.memoryleak.mvp.ui.mvp.interactor

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.Result

abstract class AbstractStopThreadInteractor : LeakInteractor {

    var thread : Thread? = null

    override operator fun invoke(
        onSuccess: ((Result.Success) -> Unit),
        onError: ((Result.Fail) -> Unit)
    ){
        startThread(onError, onSuccess)
    }

    open fun doWork(
        onError: (Result.Fail) -> Unit,
        onSuccess: (Result.Success) -> Unit
    ) : Runnable {
       return Runnable{
           try {
               Thread.sleep(20000)
               onSuccess(Result.Success("Finished doing work"))
           } catch (ex: Exception){
               if (Thread.interrupted()){
                   onError(Result.Fail(ex))
               }
           }
        }
    }

    private fun startThread(
        onError: (Result.Fail) -> Unit,
        onSuccess: (Result.Success) -> Unit
    ){
        thread.let {
            if (it != null && it.isAlive){
                it.interrupt()
            }
        }
        thread = Thread(doWork(onError, onSuccess)).also { it.start() }
    }

     override fun destroy(){
         thread?.interrupt()
         thread = null
     }
}