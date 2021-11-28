package com.example.sleepingkirby.defineevent.edit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.database.definition.event.DailyEvent

open class DailyEventListEditAdapter(private val callback: (DailyEvent) -> Unit): ListAdapter<DailyEvent, DailyEventListEditAdapter.DailyEventViewHolder>(DailyEventComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyEventViewHolder {
        return DailyEventViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DailyEventViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, callback)
    }

    class DailyEventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dailyEventItemTitleView: TextView = itemView.findViewById(R.id.daily_edit_recycler_item_title)
        private val dailyEventItemGoodTimeView: TextView = itemView.findViewById(R.id.daily_edit_recycler_item_goodtime)
        private val dailyEventItemBadTimeView: TextView = itemView.findViewById(R.id.daily_edit_recycler_item_badtime)
        private val dailyEventItemEditButon: ImageButton = itemView.findViewById(R.id.editDaily_editButton)
        private val dailyEventItemIcon: ImageView = itemView.findViewById(R.id.edit_event_item_icon)

        @SuppressLint("SetTextI18n")
        fun bind(event: DailyEvent, callback: (DailyEvent) -> Unit) {
            dailyEventItemTitleView.text = "${event.name} [${event.importance}]"
            dailyEventItemGoodTimeView.text = "GOOD:  ${if(event.goodTimeRange.isEmptyTimeRange()) "Not Specified" else event.goodTimeRange}"
            dailyEventItemBadTimeView.text = "BAD  :  ${if(event.badTimeRange.isEmptyTimeRange()) "Not Specified" else event.badTimeRange}"
            dailyEventItemIcon.setImageResource(event.icon.id)
            dailyEventItemEditButon.setOnClickListener {
                callback(event)
            }
        }

        companion object {
            fun create(parent: ViewGroup): DailyEventViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.daily_events_edit_item, parent, false)
                return DailyEventViewHolder(view)
            }
        }

    }

    class DailyEventComparator: DiffUtil.ItemCallback<DailyEvent>() {
        override fun areItemsTheSame(oldItem: DailyEvent, newItem: DailyEvent) = oldItem === newItem
        override fun areContentsTheSame(oldItem: DailyEvent, newItem: DailyEvent) = oldItem.name == newItem.name
    }
}
