package com.drawingboardapps.memoryleak.mvp.ui.mvp.view

import android.graphics.Color
import android.graphics.Color.red
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.drawingboardapps.memoryleak.mvp.memoryleak.R

import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.LeakContent.LeakItem
import com.drawingboardapps.memoryleak.mvp.ui.mvp.model.PresenterType
import com.drawingboardapps.memoryleak.mvp.ui.mvp.view.fragment.BundleArgs

/**
 * [RecyclerView.Adapter] that can display a [LeakItem].
 * TODO: Replace the implementation with code for your data type.
 */
class LeakItemRecyclerViewAdapter(
    private val values: List<LeakItem>,
    var listener: ((LeakItem) -> Unit)?
) : RecyclerView.Adapter<LeakItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_leak_chooser, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val presenterType : PresenterType = item.leakArgs[BundleArgs.PRESENTER_TYPE] as PresenterType

       when(presenterType){
           is PresenterType.Safe ->  {
               holder.leakIcon.setImageResource(R.drawable.leak_icon_no)
               holder.leakIcon.setColorFilter(Color.GREEN)
           }
           is PresenterType.Unsafe -> {
               holder.leakIcon.setImageResource(R.drawable.leak_icon_yes)
               holder.leakIcon.setColorFilter(Color.RED)
           }
       }

        holder.idView.text = item.id
        holder.contentView.text = item.getDescription()
        holder.itemView.setOnClickListener{
            listener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leakIcon: ImageView = view.findViewById(R.id.leak_icon)
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}