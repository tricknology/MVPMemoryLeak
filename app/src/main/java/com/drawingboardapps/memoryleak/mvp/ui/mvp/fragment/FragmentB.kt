package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.drawingboardapps.memoryleak.mvp.BundleArgs
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.FragmentBase
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.activity.MVPLeakActivity
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewState
import kotlinx.android.synthetic.main.fragment_basic_b.button_leak
import kotlinx.android.synthetic.main.fragment_basic_b.text

class FragmentB : FragmentBase(TAG), ViewContract, LeakContract {

    private lateinit var textViewReference: TextView
    private lateinit var leakPresenter: BasePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        return inflater.inflate(R.layout.fragment_basic_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun getFragmentViewType(): ViewContract {
        return this
    }

    override fun setActionBarTitle(text: String) {
        activity?.actionBar.let {
            Log.d(FragmentA.TAG, "setActionBarTitle: $it")
            Log.d(FragmentA.TAG, "setActionBarTitle: text $text")
            it?.title = text
        }

    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        super.onDestroyView()
        avoidLeak()
    }


    override fun updateViewState(state: ViewState) {
        Log.d(TAG, "updateViewState state - $state")
        Log.d(TAG, "updateViewState $state")

        when (state) {
            is ViewState.Update -> {
                Log.d(TAG, "updateViewState ${state.text}")
                setViewText(state.text)
                textViewReference.text = state.text
            }
        }
    }


    override fun changeView(viewComponent: FragmentType) {
        Log.d(TAG, "changeView: $viewComponent")
        Log.d(TAG, "changeView: activity = $activity")
        activity?.let {
            showFragment()
        }
    }

    override fun causeLeak() {
        Log.d(TAG, "causeLeak: presenter = $presenter")
        presenter?.causeLeak()
    }

    override fun avoidLeak() {
        Log.d(TAG, "avoidLeak: unimplemented.. presenter = $presenter")
    }

    override fun showFragment() {
        Log.d(TAG, "showFragment: activity = $activity")

        (activity as? MVPLeakActivity)?.let {
            it.fragmentRouter.showFragment(
                FragmentRouter.Params(
                    activity = it,
                    fragmentHost = R.id.main_fragment_container,
                    fragmentType = FragmentType.Leak,
                    presenterType = presenterType!!,
                    leakType = leakType!!,
                    fragmentArgs = arguments,
                    addToBackStack = false
                )
            )
        }
    }

    private fun initViews() {
        Log.d(TAG, "initViews: ")
        button_leak.setOnClickListener {
            Log.d(TAG, "button_leak clicked: ")
            causeLeak()
        }
        textViewReference = text
        textViewReference.text = """$TAG${getDetails()}"""


        Log.d(TAG, "initViews: setting action bar title...")
    }


    companion object {
        var presenterType: PresenterType? = null
        var leakType: LeakType? = null

        fun newInstance(leakType: LeakType, presenterType: PresenterType): FragmentB {
            this.presenterType = presenterType
            this.leakType = leakType

            return FragmentB().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleArgs.LEAK_TYPE, leakType)
                    putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
                }
            }
        }

        val TAG: String = FragmentB::class.simpleName!!
    }


}