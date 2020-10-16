package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class PresenterType : Parcelable {

    /**
     * Presenter implements a non-nullable view that is not de-referenced when destroy() is called
     */
    @Parcelize
    object Unsafe : PresenterType()

    /**
     * Presenter implements safety precautions to ensure that view that is de-referenced
     * when destroy() is called
     */
    @Parcelize
    object Safe : PresenterType()
}
