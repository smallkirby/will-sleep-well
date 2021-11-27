package com.example.sleepingkirby.definetask.remove

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.*
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

class RemoveTaskFragment : Fragment() {

    private val selectedTaskNames = mutableListOf<String>()

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).taskRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_remove_task, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DailyTaskListCheckboxAdapter {
            if(selectedTaskNames.contains(it)) selectedTaskNames.remove(it)
            else selectedTaskNames.add(it)
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
        for(name in selectedTaskNames) {
            dailyTaskViewModel.removeFromName(name)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RemoveTaskFragment().apply {
            }
    }
}