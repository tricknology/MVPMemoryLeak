package com.drawingboardapps.memoryleak.mvp.ui.mvp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentRouter
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.MVPLeakActivityView
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState

class MVPLeakActivity : AppCompatActivity(), MVPLeakActivityView, ViewContract {

    private var presenter: BasePresenter? = null

    var fragmentRouter: FragmentRouter = FragmentRouter
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: ")
        this.presenter = PresenterFactory.getPresenter(
            view = this,
            leakType = LeakType.ViaRunnable,
            presenterType = PresenterType.Nullable
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
                presenterType = PresenterType.NonNullable,
                leakType = LeakType.ViaRX,
                fragmentArgs = Bundle(),
                addToBackStack = false
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
        Log.d(TAG, "updateViewState: ")

    }


    override fun changeView(viewComponent: FragmentType) {
        Log.d(TAG, "changeView: ")

    }

    companion object {
        val TAG: String = MVPLeakActivity::class.simpleName!!
    }

}