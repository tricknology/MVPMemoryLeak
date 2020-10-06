package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import com.drawingboardapps.memoryleak.mvp.BundleArgs
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.FragmentBase
import com.drawingboardapps.memoryleak.mvp.base.ui.mvp.ViewContract
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.activity.MVPLeakActivity
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakContract
import kotlinx.android.synthetic.main.fragment_basic_a.*

class FragmentA : FragmentBase(TAG), ViewContract, LeakContract {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        val root = inflater.inflate(R.layout.fragment_basic_a, container, false)
        return root
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

        button_leak.setOnClickListener {
            Log.d(TAG, "button_leak clicked: ")
            causeLeak()
        }
        text.text = """$TAG${getDetails()}"""
    }

    override fun showFragment() {
        Log.d(TAG, "showFragment: ")

        (activity as? MVPLeakActivity)?.let {
            it.fragmentRouter.showFragment(
                FragmentRouter.Params(
                    activity = it,
                    fragmentHost = R.id.main_fragment_container,
                    fragmentType = FragmentType.NoLeak,
                    presenterType = PresenterType.Nullable,
                    leakType = LeakType.ViaRX,
                    fragmentArgs = Bundle().apply {
                        putParcelable(BundleArgs.PRESENTER_TYPE, PresenterType.Nullable)
                        putParcelable(BundleArgs.LEAK_TYPE, LeakType.ViaRX)
                    },
                    addToBackStack = false
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
        presenter!!.causeLeak()
        showFragment()
    }

    override fun avoidLeak() {
        Log.d(TAG, "showFragment: ")
        showFragment()
        presenter?.destroy()
        presenter = null
    }

    companion object {
        var presenterType: PresenterType? = null
        var leakType: LeakType? = null

        fun newInstance(leakType: LeakType, presenterType: PresenterType): FragmentA {
            Log.d(TAG, "newInstance: ")
            this.presenterType = presenterType
            this.leakType = leakType

            return FragmentA().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleArgs.LEAK_TYPE, leakType)
                    putParcelable(BundleArgs.PRESENTER_TYPE, presenterType)
                }
            }
        }

        val TAG: String = FragmentA::class.simpleName!!
    }
}