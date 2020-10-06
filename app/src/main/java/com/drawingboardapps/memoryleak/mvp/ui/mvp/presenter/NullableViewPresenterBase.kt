package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter


import androidx.annotation.CallSuper
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.NullableViewPresenter
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract

open class NullableViewPresenterBase<V : ViewContract>(override var view: V?) :
    NullableViewPresenter<V> {

    @CallSuper
    override fun destroy(){
        view = null
    }
}
