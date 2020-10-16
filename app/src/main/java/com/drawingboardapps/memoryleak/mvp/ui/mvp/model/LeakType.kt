package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class LeakType : Parcelable {
    @Parcelize
    object ViaRunnable : LeakType()

    @Parcelize
    object ViaCallback : LeakType()

    @Parcelize
    object ViaRX : LeakType()

}