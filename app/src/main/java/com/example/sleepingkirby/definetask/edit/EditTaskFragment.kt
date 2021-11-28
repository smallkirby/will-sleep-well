package com.example.sleepingkirby.definetask.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.SleepingApplication
import com.example.sleepingkirby.database.definition.event.DailyTask
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

class EditTaskFragment : Fragment() {

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).definitionRepository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_task, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DailyTaskListEditAdapter {
            onClickEditButton(it)
        }
        view.findViewById<RecyclerView>(R.id.edit_dailytasks_recyclerview).apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
        }

        dailyTaskViewModel.allDailyTasks.observe(viewLifecycleOwner, { tasks ->
            tasks?.let { adapter.submitList(it) }
        })
    }

    private fun onClickEditButton(task: DailyTask) {
        val action = EditTaskFragmentDirections.actionEditTaskFragmentToDoEditTaskFragment(task.id)
        view?.findNavController()?.navigate(action)
    }
}