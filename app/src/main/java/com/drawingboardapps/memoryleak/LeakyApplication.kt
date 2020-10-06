package com.drawingboardapps.memoryleak

import android.app.Application
import leakcanary.AppWatcher


class LeakyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpLeakCanary()
    }

    private fun setUpLeakCanary() {

        AppWatcher.config = AppWatcher.config.copy(
            watchFragmentViews = true,
            watchActivities = true,
            watchFragments = true,
            watchViewModels = true
        )
    }
}