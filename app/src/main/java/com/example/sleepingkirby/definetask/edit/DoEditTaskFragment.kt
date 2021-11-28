package com.example.sleepingkirby.definetask.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.sleepingkirby.R
import com.example.sleepingkirby.SleepingApplication
import com.example.sleepingkirby.database.definition.event.DailyTask
import com.example.sleepingkirby.database.definition.event.Importance
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.definition.event.dailytask.DailyTaskViewModelFactory
import com.example.sleepingkirby.definetask.add.ImportanceAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.InternalCoroutinesApi

class DoEditTaskFragment : Fragment() {

    private val args: DoEditTaskFragmentArgs by navArgs()

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).definitionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_do_edit_task, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dailyTaskViewModel.get(args.taskId) { task ->
            if (task == null) {
                Toast.makeText(this.context, "The task can't be found in DB...", Toast.LENGTH_SHORT).show()
                // XXX must go back to previous fragment
            } else {
                view.findViewById<TextInputEditText>(R.id.edit_task_name_input).setText(task.name)
                view.findViewById<EditText>(R.id.edit_task_time_input).setText(task.time)
                view.findViewById<Spinner>(R.id.edit_task_importance_spinner).apply {
                    this.adapter = ImportanceAdapter(this.context)
                    setSelection(ImportanceAdapter.importanceToIndex(task.importance))
                }

                view.findViewById<Button>(R.id.do_edit_task_button).setOnClickListener {
                    val name = view.findViewById<TextInputEditText>(R.id.edit_task_name_input).text.toString()
                    val time = view.findViewById<EditText>(R.id.edit_task_time_input).text.toString()
                    val importance = view.findViewById<Spinner>(R.id.edit_task_importance_spinner).selectedItem as Importance
                    val newTask = DailyTask(task.id, name, time, importance)
                    submitEditedTask(newTask)
                    Toast.makeText(this.context, "Completed edit of the task!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_doEditTaskFragment_to_editTaskFragment)
                }
            }
        }
    }

    @InternalCoroutinesApi
    fun submitEditedTask(task: DailyTask) {
        dailyTaskViewModel.update(task)
    }
}
