package com.example.sleepingkirby

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.database.definition.DailyDefinitionViewModel
import com.example.sleepingkirby.database.definition.DailyDefinitionViewModelFactory
import com.example.sleepingkirby.database.definition.event.DailyDefinition
import com.example.sleepingkirby.database.definition.event.DailyTask
import com.example.sleepingkirby.database.log.DailyLogViewModel
import com.example.sleepingkirby.database.log.DailyLogViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

class DailyTimelineFragment : Fragment() {

    private var definitions: List<DailyDefinition> = emptyList()

    @InternalCoroutinesApi
    private val dailyDefinitionViewmodel: DailyDefinitionViewModel by viewModels {
        DailyDefinitionViewModelFactory((activity?.application as SleepingApplication).definitionRepository)
    }
    @InternalCoroutinesApi
    private val dailyLogViewModel: DailyLogViewModel by viewModels {
        DailyLogViewModelFactory((activity?.application as SleepingApplication).logRespository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_daily_timeline, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DailyLogListAdapter(definitions)

        // observe definitions
        dailyDefinitionViewmodel.allDailyDefinitions.observe(viewLifecycleOwner, { tasks ->
            definitions = tasks
            view.findViewById<Button>(R.id.add_log_button).setOnClickListener {
                val validDefs = definitions.filter {
                    if(it.isEvent()) true else !taskIsDone(it.toDailyTask()!!)
                }
                AddLogDialogFragment(validDefs) {
                    onLogSelected(it)
                }.show(parentFragmentManager, "addlog")
            }
            adapter.updateDefinitions(definitions)
            adapter.notifyDataSetChanged()
        })

        // assign adapter to RecyclerView
        view.findViewById<RecyclerView>(R.id.dailyTaskRecyclerView).apply {
            dailyLogViewModel.todaysLogs.observe (viewLifecycleOwner, { logs ->
                adapter.submitList(logs)
            })
            this.adapter = adapter
            layoutManager= LinearLayoutManager(this.context)
        }
    }

    @InternalCoroutinesApi
    private fun onLogSelected(task: DailyDefinition) {
        dailyLogViewModel.insert(task.id)
    }

    @InternalCoroutinesApi
    private fun taskIsDone(task: DailyTask): Boolean {
        val logs = dailyLogViewModel.todaysLogs.value
        logs?.find {
            it.taskId == task.id
        }?.let { return true }
        return false
    }

    class AddLogDialogFragment(private val defs: List<DailyDefinition>, private val callback: (DailyDefinition) -> Unit): DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val options = defs.map {
                it.name
            }
            return AlertDialog.Builder(requireContext())
                .setTitle("Choose task or event to log")
                .setItems(options.toTypedArray()) { _, which ->
                    callback(defs[which])
                }
                .setNegativeButton("close", null)
                .create()
        }
    }
}