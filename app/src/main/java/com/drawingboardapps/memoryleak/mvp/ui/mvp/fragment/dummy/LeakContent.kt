package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.dummy

import android.os.Bundle
import com.drawingboardapps.memoryleak.mvp.BundleArgs
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterType
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object LeakContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<LeakItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, LeakItem> = HashMap()

    init {
        var i: Int = 0
        var iStr: String = "0"
        fun increment() {
            i = i.inc()
            iStr = i.toString()
        }

        fun createLeakArgs(leakType: LeakType, presenterType: PresenterType): Bundle {
            return Bundle().apply {
                putParcelable(BundleArgs.LEAK_TYPE, leakType)
                putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
            }
        }

        fun addLeakType(leakArgs: Bundle) {
            addItem(LeakItem(iStr, leakArgs).also { increment() })
        }

        //these do leak
        addLeakType(createLeakArgs(LeakType.ViaCallback, PresenterType.NonNullable))
        addLeakType(createLeakArgs(LeakType.ViaRX, PresenterType.NonNullable))
        addLeakType(createLeakArgs(LeakType.ViaRunnable, PresenterType.NonNullable))

        //these do not leak
        addLeakType(createLeakArgs(LeakType.ViaCallback, PresenterType.Nullable))
        addLeakType(createLeakArgs(LeakType.ViaRX, PresenterType.Nullable))
        addLeakType(createLeakArgs(LeakType.ViaRunnable, PresenterType.Nullable))

    }


    private fun addItem(item: LeakItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class LeakItem(val id: String, val leakArgs: Bundle) {
        override fun toString(): String = leakArgs.toString()

        fun getDescription(): CharSequence? {
            return StringBuilder().apply {
                append("L Type: ${leakArgs[BundleArgs.LEAK_TYPE]?.javaClass?.simpleName}\n")
                append("P Type: ${leakArgs[BundleArgs.PRESENTER_TYPE]?.javaClass?.simpleName}")
            }.toString()

        }
    }
}