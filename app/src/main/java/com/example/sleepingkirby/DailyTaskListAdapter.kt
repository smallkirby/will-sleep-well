package com.example.sleepingkirby

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.database.dailytask.DailyTask

open class DailyTaskListAdapter: ListAdapter<DailyTask, DailyTaskListAdapter.DailyTaskViewHolder>(DailyTaskComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        return DailyTaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class DailyTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dailyTaskItemView: TextView = itemView.findViewById(R.id.dailyTaskTextview)

        fun bind(task: DailyTask) {
            dailyTaskItemView.text = task.name
        }

        companion object {
            fun create(parent: ViewGroup): DailyTaskViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.daily_tasks_recyclerview_item, parent, false)
                return DailyTaskViewHolder(view)
            }
        }

    }

    class DailyTaskComparator: DiffUtil.ItemCallback<DailyTask>() {
        override fun areItemsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem === newItem
        override fun areContentsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem.name == newItem.name
    }
}