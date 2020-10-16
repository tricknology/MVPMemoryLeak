package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drawingboardapps.memoryleak.core.FragmentRouter
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.activity.MVPLeakActivity
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakContract
import kotlinx.android.synthetic.main.fragment_basic_a.*
import kotlinx.android.synthetic.main.fragment_basic_a.button_leak
import kotlinx.android.synthetic.main.fragment_basic_a.leak_icon
import kotlinx.android.synthetic.main.fragment_basic_a.text
import kotlinx.android.synthetic.main.fragment_basic_b.*

class SafeFragment : FragmentBase(TAG),
    ViewContract, LeakContract {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonPressCount = arguments?.getInt(BundleArgs.BUTTON_PRESS_COUNT) ?: 0
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        val view = inflater.inflate(R.layout.fragment_basic_a, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: ")
        super.onDestroyView()
        presenter?.destroy()
        presenter = null
    }

    private fun initViews() {
        Log.d(TAG, "initViews: ")
        setActionBarTitle(TAG)
        with(leak_icon){
            setImageResource(R.drawable.leak_icon_no)
            setColorFilter(Color.GREEN)
        }
        if (buttonPressCount > 0){
            button_leak.text = buttonPressCount.toString()
        }
        button_leak.setOnClickListener {
            Log.d(TAG, "button_leak clicked: ")
            buttonPressCount = buttonPressCount.inc()
            causeLeak()
        }
        text.text = """$TAG${getDetails()}"""
    }

    override fun showFragment() {
        Log.d(TAG, "showFragment: ")

        (activity as? MVPLeakActivity)?.let {
            it.getFragmentRouter().showFragment(
                FragmentRouter.Params(
                    activity = it,
                    fragmentHost = R.id.main_fragment_container,
                    fragmentType = FragmentType.NoLeak,
                    presenterType = presenterType!!,
                    leakType = leakType!!,
                    fragmentArgs = Bundle().apply {
                        putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
                        putParcelable(BundleArgs.LEAK_TYPE, leakType)
                        putInt(BundleArgs.BUTTON_PRESS_COUNT, buttonPressCount)
                    },
                    addToBackStack = false,
                    buttonPressCount = buttonPressCount
                )
            )
        }
    }

    override fun getFragmentViewType(): ViewContract {
        return this
    }



    override fun setActionBarTitle(text: String) {
        Log.d(TAG, "setActionBarTitle: $text")
        activity?.actionBar.let {
            Log.d(TAG, "setActionBarTitle: actionbar = $it")
            it?.title = text
        }
    }

    override fun causeLeak() {
        Log.d(TAG, "causeLeak: ")
        presenter?.causeLeak()
    }

    override fun avoidLeak() {
        Log.d(TAG, "showFragment: ")
        presenter?.destroy()
        presenter = null
    }

    companion object {
        var presenterType: PresenterType? = null
        var leakType: LeakType? = null
        var buttonPressCount: Int = 0

        fun newInstance(leakType: LeakType, presenterType: PresenterType, buttonPressCount: Int): SafeFragment {
            Log.d(TAG, "newInstance: ")
            this.presenterType = presenterType
            this.leakType = leakType
            this.buttonPressCount = buttonPressCount

            return SafeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleArgs.LEAK_TYPE, leakType)
                    putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
                    putInt(BundleArgs.BUTTON_PRESS_COUNT, buttonPressCount)
                }
            }
        }

        val TAG: String = SafeFragment::class.simpleName!!
    }
}