package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ViewState : Parcelable {
    @Parcelize
    data class Update(val text: String) : ViewState()
}
