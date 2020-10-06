package com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter

import android.os.Parcelable
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import kotlinx.android.parcel.Parcelize

sealed class PresenterLeakType : Parcelable {
    @Parcelize
    data class ViaRunnable(
        val text: String,
        val component: FragmentType.Leak
    ) : PresenterLeakType()

    @Parcelize
    data class ViaRX(
        val text: String,
        val component: FragmentType.Leak
    ) : PresenterLeakType()
}