package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.FinalViewPresenter
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract

open class FinalViewInPresenterBase<V : ViewContract>(override val view: V) :
    FinalViewPresenter<V> {
    override fun destroy() {
        //can't null the view
    }


}
