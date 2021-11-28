package com.example.sleepingkirby.definetask.remove

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.*
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

class RemoveTaskFragment : Fragment() {

    private val selectedTaskIds = mutableListOf<Int>()

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).definitionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_remove_task, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DailyTaskListCheckboxAdapter {
            if(selectedTaskIds.contains(it)) selectedTaskIds.remove(it)
            else selectedTaskIds.add(it)
        }
        view.findViewById<RecyclerView>(R.id.remove_tasks_recyclerview).apply {
            layoutManager = LinearLayoutManager(this.context)
            this.adapter = adapter
        }

        // observe livedata
        dailyTaskViewModel.allDailyTasks.observe(viewLifecycleOwner, { tasks ->
            tasks?.let { adapter.submitList(it) }
        })

        view.findViewById<Button>(R.id.button_do_remove_task).setOnClickListener {
            removeItems()
            Toast.makeText(this.context, "Removed selected tasks.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_removeTaskFragment_to_defineTaskFragment)
        }
    }

    @InternalCoroutinesApi
    private fun removeItems() {
        for(id in selectedTaskIds) {
            dailyTaskViewModel.deleteById(id)
        }
    }
}