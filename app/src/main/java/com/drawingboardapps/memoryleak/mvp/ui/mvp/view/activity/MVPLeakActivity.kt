package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.core.FragmentRouter
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.MVPLeakActivityView
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.ViewState

class MVPLeakActivity : AppCompatActivity(), MVPLeakActivityView, ViewContract {

    private var presenter: BasePresenter? = null

    private var fragmentRouter: FragmentRouter =
        FragmentRouter

    override fun getFragmentRouter(): FragmentRouter {
        return fragmentRouter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: ")
        this.presenter = PresenterFactory.getPresenter(
            view = this,
            leakType = LeakType.ViaRunnable,
            presenterType = PresenterType.Safe
        )
        initViews()
    }

    private fun initViews() {
        Log.d(TAG, "initViews: ")

        fragmentRouter.showFragment(
            FragmentRouter.Params(
                activity = this,
                fragmentHost = R.id.main_fragment_container,
                fragmentType = FragmentType.Chooser,
                presenterType = PresenterType.Unsafe,
                leakType = LeakType.ViaRX,
                fragmentArgs = Bundle(),
                addToBackStack = false,
                buttonPressCount = 0
            )
        )
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        avoidLeak()
    }

    override fun causeLeak() {
        Log.d(TAG, "causeLeak: ")
        presenter?.causeLeak()
    }

    override fun avoidLeak() {
        Log.d(TAG, "avoidLeak: ")
        presenter = null
    }

    override fun updateViewState(state: ViewState) {
        Log.d(TAG, "updateViewState: unimplemented")
    }


    override fun changeView(viewComponent: FragmentType) {
        Log.d(TAG, "changeView: default implementation")
//        fragmentRouter.showFragment(
//            FragmentRouter.Params(
//                activity = this,
//                fragmentHost = R.id.main_fragment_container,
//                fragmentType = viewComponent,
//                presenterType = PresenterType.Safe,
//                leakType = LeakType.ViaCallback,
//                fragmentArgs = Bundle(),
//                addToBackStack = false
//            )
//        )
    }

    companion object {
        val TAG: String = MVPLeakActivity::class.simpleName!!
    }

}