package com.example.sleepingkirby.defineevent.add

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.SleepingApplication
import com.example.sleepingkirby.database.definition.event.*
import com.example.sleepingkirby.definetask.add.ImportanceAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*

class AddEventFragment : Fragment() {

    private var currentSelectedIcon: EventIcon? = null

    @InternalCoroutinesApi
    private val dailyEventViewModel: DailyEventViewModel by viewModels {
        DailyEventViewModelFactory((activity?.application as SleepingApplication).definitionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_event, container, false)
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set importance spinner items
        view.findViewById<Spinner>(R.id.add_event_importance_spinner).apply {
            this.adapter = ImportanceAdapter(this.context)
        }

        // set TimePickers
        arrayOf(R.id.add_even_goodtime_start_button, R.id.add_even_goodtime_end_button, R.id.add_even_badtime_start_button, R.id.add_even_badtime_end_button).forEach {
            view.findViewById<Button>(it).apply {
                setOnClickListener {
                    TimePickerFragment(this).show(parentFragmentManager, "timePicker")
                }
            }
        }

        // set RecyclerView for EventIcon
        view.findViewById<RecyclerView>(R.id.event_icons_recycler).apply {
            this.adapter = IconsListAdapter {
                clearAllColorFilter()
                onIconButtonClicked(it)
            }
            layoutManager = GridLayoutManager(this.context, 6)
        }

        // set submission Button
        view.findViewById<Button>(R.id.add_event_button).setOnClickListener {
            onSubmission()
        }
    }

    private fun onIconButtonClicked(iconId: Int): Boolean {
        val eventIcon = EventIcon.fromId(iconId)
        return if(currentSelectedIcon == eventIcon){
            currentSelectedIcon = null
            false
        } else {
            currentSelectedIcon = eventIcon
            true
        }
    }

    private fun clearAllColorFilter() {
        val recycler = view?.findViewById<RecyclerView>(R.id.event_icons_recycler) ?: return
        for(i in 0 until recycler.childCount) {
            val holder = recycler.getChildViewHolder(recycler.getChildAt(i)) as IconsListAdapter.ViewHolder
            holder.button.clearColorFilter()
        }
    }

    @InternalCoroutinesApi
    private fun onSubmission() {
        // get inputs from Views
        val view = view ?: return
        val name = view.findViewById<EditText>(R.id.add_event_name_input).text.toString()
        val icon = currentSelectedIcon ?: EventIcon.OTHER
        val importance = view.findViewById<Spinner>(R.id.add_event_importance_spinner).selectedItem as Importance
        val goodTimeStartStr = view.findViewById<Button>(R.id.add_even_goodtime_start_button).text.toString()
        val goodTimeEndStr = view.findViewById<Button>(R.id.add_even_goodtime_end_button).text.toString()
        val badTimeStartStr = view.findViewById<Button>(R.id.add_even_badtime_start_button).text.toString()
        val badTimeEndStr = view.findViewById<Button>(R.id.add_even_badtime_end_button).text.toString()

        // construct timerange
        val goodTimeRange = TimeRange.fromString(goodTimeStartStr, goodTimeEndStr)
        val badTimeRange = TimeRange.fromString(badTimeStartStr, badTimeEndStr)

        // check validity
        if(name.isEmpty()) {
            Toast.makeText(this.context, "Please enter event name.", Toast.LENGTH_SHORT).show()
            return
        }

        // submit new event to DB
        val event = DailyEvent(0, name, goodTimeRange, badTimeRange, importance, icon)
        dailyEventViewModel.insert(event)
        Toast.makeText(this.context, "New event is created!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addEventFragment_to_defineEventFragment)
    }

    class TimePickerFragment(private val timeButton: Button): DialogFragment(), TimePickerDialog.OnTimeSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
        }

        @SuppressLint("SetTextI18n")
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            timeButton.text = "$hourOfDay:$minute"
            Logger.d(arrayOf(hourOfDay, minute))
        }
    }
}