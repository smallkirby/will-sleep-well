package com.example.sleepingkirby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

class DailyTimelineFragment : Fragment() {

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).taskRepository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_timeline, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // assign adapter to daily-tasks viewmodel
        val dailyTasksRecyclerView = view.findViewById<RecyclerView>(R.id.dailyTaskRecyclerView)
        val adapter = DailyTaskListAdapter()
        dailyTasksRecyclerView.adapter = adapter
        dailyTasksRecyclerView.layoutManager = LinearLayoutManager(this.context)

        // observe livedata
        dailyTaskViewModel.allDailyTasks.observe(viewLifecycleOwner, { tasks ->
            tasks?.let { adapter.submitList(it) }
        })

    }
}