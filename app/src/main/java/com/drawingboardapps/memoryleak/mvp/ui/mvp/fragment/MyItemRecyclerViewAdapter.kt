package com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.drawingboardapps.memoryleak.mvp.memoryleak.R
import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.dummy.LeakContent

import com.drawingboardapps.memoryleak.mvp.ui.mvp.fragment.dummy.LeakContent.LeakItem

/**
 * [RecyclerView.Adapter] that can display a [LeakItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val values: List<LeakItem>,
    var listener: ((LeakItem) -> Unit)?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_leak_chooser, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.getDescription()
        holder.itemView.setOnClickListener{
            listener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}