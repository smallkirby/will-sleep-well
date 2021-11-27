package com.example.sleepingkirby.definetask.remove

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.database.dailytask.DailyTask

open class DailyTaskListCheckboxAdapter(private val callback: (name: String) -> Unit): ListAdapter<DailyTask, DailyTaskListCheckboxAdapter.DailyTaskViewHolder>(DailyTaskComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        return DailyTaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, callback)
    }

    class DailyTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dailyTaskItemView: CheckBox = itemView.findViewById(R.id.task_recyclerview_checkbox)
        val name: String
            get() = dailyTaskItemView.text.toString()

        fun bind(task: DailyTask, callback: (String) -> Unit) {
            dailyTaskItemView.text = task.name
            dailyTaskItemView.setOnClickListener {
                callback(task.name)
            }
        }

        fun isSelected() = dailyTaskItemView.isSelected

        companion object {
            fun create(parent: ViewGroup): DailyTaskViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.tasks_checkbox_recyclerview_item, parent, false)
                return DailyTaskViewHolder(view)
            }
        }

    }

    class DailyTaskComparator: DiffUtil.ItemCallback<DailyTask>() {
        override fun areItemsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem === newItem
        override fun areContentsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem.name == newItem.name
    }
}
