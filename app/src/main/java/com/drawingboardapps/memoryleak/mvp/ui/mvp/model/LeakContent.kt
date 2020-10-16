package com.drawingboardapps.memoryleak.mvp.ui.mvp.model

import android.os.Bundle
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.BundleArgs
import java.util.ArrayList
import java.util.HashMap

/**
 * Contains hardcoded content regarding the types of leaks possible with this app
 */
object LeakContent {

    val ITEMS: MutableList<LeakItem> = ArrayList()

    private val ITEM_MAP: MutableMap<String, LeakItem> = HashMap()

    init {
        var index = 0
        var iStr = "0"
        fun increment() {
            index = index.inc()
            iStr = index.toString()
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
        addLeakType(createLeakArgs(LeakType.ViaCallback, PresenterType.Unsafe))
        addLeakType(createLeakArgs(LeakType.ViaRX, PresenterType.Unsafe))
        addLeakType(createLeakArgs(LeakType.ViaRunnable, PresenterType.Unsafe))

        //these do not leak
        addLeakType(createLeakArgs(LeakType.ViaCallback, PresenterType.Safe))
        addLeakType(createLeakArgs(LeakType.ViaRX, PresenterType.Safe))
        addLeakType(createLeakArgs(LeakType.ViaRunnable, PresenterType.Safe))

    }


    private fun addItem(item: LeakItem) {
        ITEMS.add(item)
        ITEM_MAP[item.id] = item
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class LeakItem(val id: String = "", val leakArgs: Bundle) {
        override fun toString(): String = leakArgs.toString()

        fun getDescription(): CharSequence? {
            val presenterType: PresenterType? =
                leakArgs[BundleArgs.PRESENTER_TYPE] as? PresenterType
            val leakType: LeakType? = leakArgs[BundleArgs.LEAK_TYPE] as? LeakType

            return StringBuilder()
                .apply {
                    append("Leak Type            : ${leakArgs[BundleArgs.LEAK_TYPE]?.javaClass?.simpleName}\n")
                    append("Presenter View   : ${leakArgs[BundleArgs.PRESENTER_TYPE]?.javaClass?.simpleName}\n")
                    append("Leakable              : ${canLeakTypeLeadToLeak(leakType)}\n")
                    append("Does Leak           : ${shouldPresenterLeak(presenterType)}")
                }.toString()
        }

        companion object{
            fun canLeakTypeLeadToLeak(leakType: LeakType?): Boolean? {
                return when (leakType) {
                    is LeakType.ViaCallback -> true
                    is LeakType.ViaRX -> true
                    is LeakType.ViaRunnable -> true
                    else -> null
                }
            }

            fun shouldPresenterLeak(presenterType: PresenterType?): Boolean? {
                return when (presenterType) {
                    is PresenterType.Unsafe -> true
                    is PresenterType.Safe -> false
                    else -> null
                }
            }
        }

    }
}