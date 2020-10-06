package com.drawingboardapps.memoryleak.mvp.base.ui.mvp

import com.drawingboardapps.memoryleak.mvp.base.core.Destroyable

interface NullableViewPresenter<V : ViewContract?> :
    Destroyable {
    var view: V?
}

interface FinalViewPresenter<V : ViewContract> :
    Destroyable {
    val view: V
}