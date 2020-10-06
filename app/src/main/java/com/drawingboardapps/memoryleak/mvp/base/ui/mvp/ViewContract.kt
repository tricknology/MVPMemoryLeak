package com.drawingboardapps.memoryleak.mvp.base.ui.mvp

import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState

interface ViewContract  {
    fun updateViewState(state: ViewState)
    fun changeView(viewComponent: FragmentType)
}
