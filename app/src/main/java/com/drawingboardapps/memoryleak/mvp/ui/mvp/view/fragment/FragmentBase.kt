package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.*
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.ViewContract
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.BasePresenter
import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterFactory

abstract class FragmentBase(
    private val name: String
) : Fragment(),
    ViewContract {

    var presenter : BasePresenter? = null

    protected abstract fun setActionBarTitle(text: String)

    protected abstract fun showFragment()

    abstract fun getFragmentViewType(): ViewContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let{
            val presenterType : PresenterType = it.getParcelable(BundleArgs.PRESENTER_TYPE)!!
            val leakType : LeakType = it.getParcelable(BundleArgs.LEAK_TYPE)!!

            presenter = PresenterFactory.getPresenter(getFragmentViewType(),  leakType, presenterType)
            Log.d("BaseFragment", "presenterType: $presenterType")
            Log.d("BaseFragment", "leakType: $leakType")
            Log.d("BaseFragment", "presenter: $presenter")

        }
    }

    override fun changeView(viewComponent: FragmentType) {
        Log.d(name, "changeView: ")
        showFragment()
    }

    override fun updateViewState(state: ViewState) {
        Log.d(name, "updateViewState: ")

        when(state){
            is ViewState.Update -> {
                setViewText(state.text)
            }
        }
    }
    open fun setViewText(text: String) {
        Log.d(name, "setViewText: ")
        setActionBarTitle(text)
    }

    fun getDetails() : String? {
       return arguments?.let {
            return@let StringBuilder().apply {
                append("\nDescription: \n")
                append("${LeakContent.LeakItem(leakArgs = it).getDescription()}")
                append("\n")
            }.toString()
        }
    }


}
