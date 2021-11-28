package com.example.sleepingkirby

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.database.definition.event.DailyDefinition
import com.example.sleepingkirby.database.log.DailyLog
import java.text.SimpleDateFormat
import java.util.*

open class DailyLogListAdapter(private var definitions: List<DailyDefinition>): ListAdapter<DailyLog, DailyLogListAdapter.DailyLogViewHolder>(DailyLogComparator()) {

    fun updateDefinitions(newDefinitions: List<DailyDefinition>) {
        definitions = newDefinitions
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyLogViewHolder {
        return DailyLogViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DailyLogViewHolder, position: Int) {
        val log = getItem(position)
        val definition = definitions.find {
            it.id == log.taskId
        }
        holder.bind(log, definition)
    }

    class DailyLogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dailyTaskItemView: TextView = itemView.findViewById(R.id.dailyTaskTextview)

        @SuppressLint("SetTextI18n")
        fun bind(log: DailyLog, definition: DailyDefinition?) {
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            if(definition != null) {
                val icon = if(definition.isTask()) {
                    R.drawable.ic_check
                } else {
                    definition.icon.id
                }
                dailyTaskItemView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
            }
            dailyTaskItemView.text = "  ${dateFormat.format(log.time)}  ${definition?.name}"
        }

        companion object {
            fun create(parent: ViewGroup): DailyLogViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.daily_logs_recyclerview_item, parent, false)
                return DailyLogViewHolder(view)
            }
        }

    }

    class DailyLogComparator: DiffUtil.ItemCallback<DailyLog>() {
        override fun areItemsTheSame(oldItem: DailyLog, newItem: DailyLog) = oldItem === newItem
        override fun areContentsTheSame(oldItem: DailyLog, newItem: DailyLog) = oldItem.id == newItem.id
    }
}