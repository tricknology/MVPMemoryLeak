package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class PresenterType : Parcelable {
    /**
     * Presenter implements a nullable view that is de-referenced when destroy() is called
     */
    @Parcelize
    object Nullable : PresenterType()
    /**
     * Presenter implements a non-nullable view that is not de-referenced when destroy() is called
     */
    @Parcelize
    object NonNullable : PresenterType()
    /**
     * Presenter implements various safety precautions to ensure that view that is de-referenced
     * when destroy() is called
     */
    @Parcelize
    object Safe : PresenterType()
}
