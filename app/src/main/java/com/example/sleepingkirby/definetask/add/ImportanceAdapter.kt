package com.example.sleepingkirby.definetask.add

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.example.sleepingkirby.database.dailytask.Importance

class ImportanceAdapter(val context: Context): SpinnerAdapter, BaseAdapter() {

    override fun getCount() = Importance.values().size

    override fun getItem(position: Int) = Importance.values()[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textview = TextView(context)
        textview.text = Importance.values()[position].name
        return textview
    }

    companion object {
        fun importanceToIndex(importance: Importance) = Importance.values().indexOf(importance)
    }
}
