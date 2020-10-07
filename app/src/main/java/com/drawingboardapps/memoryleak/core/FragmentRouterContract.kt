package com.drawingboardapps.memoryleak.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType

interface FragmentRouterContract {

    fun getFragment(
        activity: AppCompatActivity,
        component: FragmentType
    ): Fragment?


    fun showFragment(routerParams: FragmentRouter.Params)

    fun popBackstack(activity: AppCompatActivity, tag: String?, flag: Int?)
}