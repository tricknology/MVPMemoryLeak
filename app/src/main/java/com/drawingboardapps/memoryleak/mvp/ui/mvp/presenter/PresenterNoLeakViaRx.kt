package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.core.rx.SchedulerProvider
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState

class PresenterNoLeakViaRx(var view : ViewContract?,
                           val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : BasePresenter(), ViewContract {

    companion object {
        val TAG: String = PresenterNoLeakViaRx::class.simpleName!!
    }

    override fun destroy() {
        view = null
    }

    override fun causeLeak() {

    }

    override fun avoidLeak() {
    }

    override fun updateViewState(state: ViewState) {
    }


    override fun changeView(viewComponent: FragmentType) {
    }
}