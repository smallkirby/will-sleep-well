package com.example.sleepingkirby.definetask.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.database.dailytask.DailyTask

open class DailyTaskListEditAdapter(val callback: (DailyTask) -> Unit): ListAdapter<DailyTask, DailyTaskListEditAdapter.DailyTaskViewHolder>(DailyTaskComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyTaskViewHolder {
        return DailyTaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DailyTaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, callback)
    }

    class DailyTaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val dailyTaskItemTitleView: TextView = itemView.findViewById(R.id.daily_edit_recycler_item_title)
        private val dailyTaskItemDescriptionView: TextView = itemView.findViewById(R.id.daily_edit_recycler_item_goodtime)
        private val dailyTaskItemEditButon: ImageButton = itemView.findViewById(R.id.editDaily_editButton)

        fun bind(task: DailyTask, callback: (DailyTask) -> Unit) {
            dailyTaskItemTitleView.text = task.name
            dailyTaskItemDescriptionView.text = "@${task.time} [${task.importance}]"
            dailyTaskItemEditButon.setOnClickListener {
                callback(task)
            }
        }

        companion object {
            fun create(parent: ViewGroup): DailyTaskViewHolder {
                val view: View = LayoutInflater.from(parent.context).inflate(R.layout.daily_tasks_edit_item, parent, false)
                return DailyTaskViewHolder(view)
            }
        }

    }

    class DailyTaskComparator: DiffUtil.ItemCallback<DailyTask>() {
        override fun areItemsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem === newItem
        override fun areContentsTheSame(oldItem: DailyTask, newItem: DailyTask) = oldItem.name == newItem.name
    }
}
