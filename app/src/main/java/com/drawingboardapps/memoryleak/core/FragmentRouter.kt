package com.drawingboardapps.memoryleak.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.FragmentsFactory
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType

object FragmentRouter :
    FragmentRouterContract {

    /**
     * @param activity
     * @param presenterType
     * @param fragmentHost
     * @param fragmentArgs
     * @param addToBackStack
     */
    data class Params(
        val activity: AppCompatActivity,
        val fragmentHost: Int,
        val fragmentType: FragmentType,
        val presenterType: PresenterType,
        val leakType: LeakType,
        val fragmentArgs: Bundle?,
        val addToBackStack: Boolean
    )

    /**
     * Search the backstack for the fragment
     * @param activity
     * @param component
     * @return the fragment or null if not found
     */
    override fun getFragment(
        activity: AppCompatActivity,
        component: FragmentType
    ): Fragment? {
        return activity.supportFragmentManager.findFragmentByTag(component.tag)
    }


    /**
     * Show the fragment in the specified host container
     * Note: commits allowing state loss
     * @param routerParams
     */
    override fun showFragment(routerParams: Params
    ) {
        with(routerParams){
            activity.supportFragmentManager.beginTransaction().apply {

                val fragment =
                    FragmentsFactory.getFragment(
                        fragmentType,
                        presenterType,
                        leakType
                    )
                if (addToBackStack) addToBackStack(fragment.tag)
                replace(fragmentHost, fragment, fragmentType.tag)
            }.commitAllowingStateLoss()
        }
    }


    /**
     * Pops the backstack immediately
     * @param activity
     * @param tag
     * @param flag defaults to [POP_BACK_STACK_INCLUSIVE]
     */
    override fun popBackstack(activity: AppCompatActivity, tag: String?, flag: Int?) {
        activity.supportFragmentManager.popBackStackImmediate(
            tag,
            flag ?: POP_BACK_STACK_INCLUSIVE
        )
    }




}