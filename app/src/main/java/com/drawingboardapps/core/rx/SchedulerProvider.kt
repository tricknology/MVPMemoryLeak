package com.drawingboardapps.core.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers


interface RxSchedulerProvider  {
    fun ui(): Scheduler?
    fun computation(): Scheduler?
    fun io(): Scheduler?

}
class SchedulerProvider :
    RxSchedulerProvider {
    override fun ui(): Scheduler? = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler? = Schedulers.computation()

    override fun io(): Scheduler?  = Schedulers.io()

}