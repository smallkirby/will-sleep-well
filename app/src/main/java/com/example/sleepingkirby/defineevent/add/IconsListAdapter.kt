package com.example.sleepingkirby.defineevent.add

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.database.dailyevent.EventIcon

class IconsListAdapter(private val callback: (id: Int) -> Boolean): RecyclerView.Adapter<IconsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: ImageButton

        init {
            button = view.findViewById(R.id.iconsRecyclerImage)
            button.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            //.inflate(R.layout.icons_recycler_item, viewGroup, false)
            .inflate(R.layout.icons_recycler_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.button.apply {
            val iconId = EventIcon.values()[position].id
            setBackgroundResource(iconId)
            setOnClickListener {
                if(callback(iconId)) this.setColorFilter(ContextCompat.getColor(this.context, R.color.purple_700), PorterDuff.Mode.SRC_ATOP)
                else this.clearColorFilter()
                this.setImageResource(iconId)
            }
        }
    }

    override fun getItemCount() = EventIcon.values().size

}
