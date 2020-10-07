package com.drawingboardapps.memoryleak.mvp.ui.mvp.view

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState

interface ViewContract  {
    fun updateViewState(state: ViewState)
    fun changeView(viewComponent: FragmentType)
}
