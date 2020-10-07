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
                    is PresenterType.Nullable ->   FragmentType.NoLeak
                    is PresenterType.NonNullable ->   FragmentType.Leak
                    else -> FragmentType.NoLeak
                }

            a.getFragmentRouter().showFragment(
                FragmentRouter.Params(
                    activity = a,
                    fragmentHost = R.id.main_fragment_container,
                    fragmentType = fragmentType,
                    presenterType = presenterType,
                    leakType = leakType,
                    fragmentArgs = arguments,
                    addToBackStack = false
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leak_chooser_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                itemAdapter =
                    LeakItemRecyclerViewAdapter(
                        LeakContent.ITEMS,
                        itemClickListener
                    )
                adapter = itemAdapter
            }
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemClickListener = null
        itemAdapter?.listener = itemClickListener
        itemAdapter = null
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        val TAG: String = LeakChooserFragment::class.java.simpleName

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() =
            LeakChooserFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}