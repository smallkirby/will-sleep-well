package com.example.sleepingkirby.definetask.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sleepingkirby.R
import com.example.sleepingkirby.SleepingApplication
import com.example.sleepingkirby.database.dailytask.DailyTask
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModel
import com.example.sleepingkirby.database.dailytask.DailyTaskViewModelFactory
import com.example.sleepingkirby.database.dailytask.Importance
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.NumberFormatException

class AddTaskFragment : Fragment() {

    @InternalCoroutinesApi
    private val dailyTaskViewModel: DailyTaskViewModel by viewModels {
        DailyTaskViewModelFactory((activity?.application as SleepingApplication).taskRepository)
    }
    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        val importanceSpinner = view.findViewById<Spinner>(R.id.add_task_importance_spinner)
        importanceSpinner.adapter = ImportanceAdapter(this.requireContext())

        val submitButton = view.findViewById<Button>(R.id.add_task_button)
        submitButton.setOnClickListener {
            submitTaskCallback(view)
        }

        return view
    }

    @InternalCoroutinesApi
    private fun submitTaskCallback(view: View) {
        val name = view.findViewById<TextInputEditText>(R.id.add_task_name_input).text.toString().trim()
        val time = view.findViewById<EditText>(R.id.add_task_time_input).text.toString().trim() // XXX should use TimePicker widget
        val importance = view.findViewById<Spinner>(R.id.add_task_importance_spinner).selectedItem as Importance

        val fmtError = isValuesValid(name, time)
        if (fmtError.isNotEmpty()) {
            Toast.makeText(this.context, fmtError, Toast.LENGTH_SHORT).show()
            return
        }

        // register task and go back
        val task = DailyTask(0, name, time, importance)
        dailyTaskViewModel.insert(task)

        findNavController().navigate(R.id.action_addTaskFragment_to_defineTaskFragment)
    }

    // XXX must check duplication of name with existing ones.
    private fun isValuesValid(name: String, time: String): String {
        return if (name.isBlank()) "Please specify name."
            else if (name.contains("\n")) "Name must be singleline." // XXX must restrict on UI
            else if (!isTimeValid(time)) "Time is invalid."
            else ""
    }

    private fun isTimeValid(stime: String): Boolean {
        val parts = stime.split(":")
        if(parts.size != 2) return false
        return try {
            val hour = parts[0].toInt()
            val minute = parts[1].toInt()
            (0..23).contains(hour) && (0..59).contains(minute)
        } catch (nfe: NumberFormatException) {
            false
        }
    }
}