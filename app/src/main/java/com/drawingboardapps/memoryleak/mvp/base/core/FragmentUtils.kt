package com.drawingboardapps.memoryleak.mvp.base.core

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.drawingboardapps.memoryleak.mvp.base.core.util.CommonUtils

object FragmentUtils {

    @JvmOverloads
    fun add(
        AppCompatActivity: AppCompatActivity,
        fragment: Fragment,
        containerViewId: Int,
        addToStack: Boolean,
        clearStack: Boolean = false
    ) {
        if (isContextInvalid(AppCompatActivity)) {
            return
        }
        CommonUtils.hideSoftKeyboard(AppCompatActivity)
        val fragmentManager =
            AppCompatActivity.supportFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        if (fragmentManager.backStackEntryCount > 0 && clearStack) {
            clearStack(AppCompatActivity)
        }
        fragmentTransaction.add(containerViewId, fragment)
        if (addToStack) {
            var backStackName = getCurrentFragment(AppCompatActivity)
            backStackName =
                if (!TextUtils.isEmpty(backStackName)) backStackName else fragment.javaClass.simpleName
            fragmentTransaction.addToBackStack(backStackName)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun replace(
        AppCompatActivity: AppCompatActivity,
        fragment: Fragment,
        containerViewId: Int,
        addToStack: Boolean,
        animationType: String?,
        clearStack: Boolean
    ) {
        if (isContextInvalid(AppCompatActivity)) {
            return
        }
        CommonUtils.hideSoftKeyboard(AppCompatActivity)
        val fragmentManager =
            AppCompatActivity.supportFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        if (fragmentManager.backStackEntryCount > 0 && clearStack) {
            clearStack(AppCompatActivity)
        }
        fragmentTransaction.replace(containerViewId, fragment)
        if (addToStack) {
            var backStackName: String? = null
            backStackName =
                if (!TextUtils.isEmpty(backStackName)) backStackName else fragment.javaClass.simpleName
            fragmentTransaction.addToBackStack(backStackName)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun remove(
        AppCompatActivity: AppCompatActivity,
        fragment: Fragment?
    ) {
        if (isContextInvalid(AppCompatActivity)) {
            return
        }
        if (fragment != null && fragment.isAdded) {
            AppCompatActivity.supportFragmentManager.beginTransaction().remove(fragment)
                .commitAllowingStateLoss()
        }
    }

    fun removeUsingChildFragmentManager(
        AppCompatActivity: AppCompatActivity?,
        childFragmentManager: FragmentManager,
        fragment: Fragment?
    ) {
        if (isContextInvalid(AppCompatActivity)) {
            return
        }
        if (fragment != null && fragment.isAdded) {
            childFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
    }

    fun show(
        AppCompatActivity: AppCompatActivity,
        fragment: Fragment
    ) {
        if (fragment.isAdded) {
            AppCompatActivity.supportFragmentManager.beginTransaction().show(fragment)
                .commitAllowingStateLoss()
        }
    }

    fun hide(
        AppCompatActivity: AppCompatActivity,
        fragment: Fragment
    ) {
        if (fragment.isAdded) {
            AppCompatActivity.supportFragmentManager.beginTransaction().hide(fragment)
                .commitAllowingStateLoss()
        }
    }

    fun getCurrentFragment(AppCompatActivity: AppCompatActivity): String? {
        val fragmentManager =
            AppCompatActivity.supportFragmentManager
        return if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1)
                .name
        } else ""
    }

    fun getBackStackCount(AppCompatActivity: AppCompatActivity): Int {
        return AppCompatActivity.supportFragmentManager.backStackEntryCount
    }

    /**
     * Removing all the fragments that are showing here
     *
     * @param AppCompatActivity
     */
    fun removeStack(AppCompatActivity: AppCompatActivity) {
        if (isContextInvalid(AppCompatActivity)) {
            return
        }
        val fragmentManager =
            AppCompatActivity.supportFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        val fragments =
            fragmentManager.fragments
        if (fragments != null && fragments.size > 0) {
            for (i in fragments.indices) {
                val mFragment = fragments[i]
                if (mFragment != null && mFragment.isAdded) {
                    fragmentTransaction.remove(mFragment).commitAllowingStateLoss()
                }
            }
        }
    }

    fun clearBackStack(AppCompatActivity: AppCompatActivity?) {
        if (AppCompatActivity == null) return
        val fm = AppCompatActivity.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    fun clearStack(AppCompatActivity: AppCompatActivity) {
        AppCompatActivity.supportFragmentManager.popBackStackImmediate(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    fun popBackStack(AppCompatActivity: AppCompatActivity) {
        AppCompatActivity.supportFragmentManager.popBackStackImmediate()
    }

    fun isContextInvalid(context: AppCompatActivity?): Boolean {
        return context == null || context.isFinishing
    }
}