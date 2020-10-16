package com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.drawingboardapps.memoryleak.core.FragmentRouter
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.FragmentType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.activity.MVPLeakActivity
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakContent
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.LeakItemRecyclerViewAdapter

/**
 * A fragment representing a list of Items.
 */
class LeakChooserFragment : Fragment() {

    private var itemAdapter: LeakItemRecyclerViewAdapter? = null
    private var columnCount = 1

    private var itemClickListener: ((LeakContent.LeakItem) -> Unit)? = {
        (activity as? MVPLeakActivity)?.let { a ->
            val presenterType : PresenterType = (it.leakArgs[BundleArgs.PRESENTER_TYPE] as PresenterType)
            val leakType : LeakType = (it.leakArgs[BundleArgs.LEAK_TYPE] as LeakType)


            val fragmentType: FragmentType =
                when(presenterType){
                    is PresenterType.Safe ->   FragmentType.NoLeak
                    is PresenterType.Unsafe ->   FragmentType.Leak
                }

            a.getFragmentRouter().showFragment(
                FragmentRouter.Params(
                    activity = a,
                    fragmentHost = R.id.main_fragment_container,
                    fragmentType = fragmentType,
                    presenterType = presenterType,
                    leakType = leakType,
                    fragmentArgs = arguments,
                    addToBackStack = false,
                    buttonPressCount = 0
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leak_chooser_list, container, false)

        // Set the adapter
        setUpRecyclerViewAdapter(view)
        return view
    }

    private fun setUpRecyclerViewAdapter(view: View) {
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                itemAdapter = LeakItemRecyclerViewAdapter(LeakContent.ITEMS, itemClickListener)
                adapter = itemAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemClickListener = null
        itemAdapter?.listener = itemClickListener
        itemAdapter = null
    }

    companion object {

        val TAG: String = LeakChooserFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = LeakChooserFragment()
    }
}