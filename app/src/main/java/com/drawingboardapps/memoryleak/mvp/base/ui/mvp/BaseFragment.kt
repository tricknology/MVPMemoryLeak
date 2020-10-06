//package com.drawingboardapps.memoryleak.mvp.base.ui.mvp
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import com.drawingboardapps.memoryleak.mvp.BundleArgs
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentB
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.FragmentType
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.BasePresenter
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.LeakType
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterFactory
//import com.drawingboardapps.memoryleak.mvp.ui.mvp.presenter.PresenterType
//
//abstract class BaseFragment : Fragment() {
//    fun logd(tag : String, message: String){
//        Log.d(tag, message)

//    lateinit var presenter : BasePresenter//    }
////
//
//    abstract fun getFragmentViewType() : ViewContract
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.d(name, "onCreate: ${getFragmentViewType()}")
//        super.onCreate(savedInstanceState)
//        requireArguments().let{
//            val presenterType : PresenterType = it.getParcelable(BundleArgs.PRESENTER_TYPE)!!
//            val leakType : LeakType = it.getParcelable(BundleArgs.LEAK_TYPE)!!
//            presenter = PresenterFactory.getPresenter(getFragmentViewType(),  leakType, presenterType)
//            Log.d("BaseFragment", "presenterType: $presenterType")
//            Log.d("BaseFragment", "leakType: $leakType")
//            Log.d("BaseFragment", "presenter: $presenter")
//
//        }
//    }
//}