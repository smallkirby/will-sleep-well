package com.example.sleepingkirby.defineevent.edit

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sleepingkirby.R
import com.example.sleepingkirby.SleepingApplication
import com.example.sleepingkirby.database.dailyevent.*
import com.example.sleepingkirby.database.dailytask.Importance
import com.example.sleepingkirby.defineevent.add.IconsListAdapter
import com.example.sleepingkirby.definetask.add.ImportanceAdapter
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.InternalCoroutinesApi

class DoEditEventFragment : Fragment() {

    private var currentSelectedIcon: EventIcon? = null
    private val args: DoEditEventFragmentArgs by navArgs()

    @InternalCoroutinesApi
    private val dailyEventViewModel: DailyEventViewModel by viewModels {
        DailyEventViewModelFactory((activity?.application as SleepingApplication).eventRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_event, container, false)
        return view
    }

    @InternalCoroutinesApi
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // replace texts for edit
        view.findViewById<TextView>(R.id.title_add_event).text = "Edit Daily Event"
        view.findViewById<TextView>(R.id.add_event_button).text = "SUBMIT"
        view.findViewById< RecyclerView>(R.id.event_icons_recycler).apply {
            this.adapter = IconsListAdapter {
                clearAllColorFilter()
                onIconButtonClicked(it)
            }
            layoutManager = GridLayoutManager(this.context, 6)
        }

        // add placeholder with specified event
        setPlaceholder(view)
    }

    @InternalCoroutinesApi
    private fun onSubmission(oldEvent: DailyEvent) {
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
        val event = DailyEvent(oldEvent.id, name, goodTimeRange, badTimeRange, importance, icon)
        dailyEventViewModel.update(event)
        Toast.makeText(this.context, "Update is completed!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_doEditEventFragment_to_editEventFragment)
    }

    @InternalCoroutinesApi
    private fun setPlaceholder(view: View) {
        dailyEventViewModel.get(args.eventId) { event ->
            if (event == null) {
                Toast.makeText(this.context, "The event can't be found in DB...", Toast.LENGTH_SHORT).show()
                // XXX must go back to previous fragment
            } else {
                // set placeholders
                view.findViewById<TextInputEditText>(R.id.add_event_name_input).setText(event.name)
                view.findViewById<Spinner>(R.id.add_event_importance_spinner).apply {
                    this.adapter = ImportanceAdapter(this.context)
                    setSelection(ImportanceAdapter.importanceToIndex(event.importance))
                }
                view.findViewById< RecyclerView>(R.id.event_icons_recycler).apply {
                    val holder = getChildViewHolder(getChildAt(event.icon.ordinal)) as IconsListAdapter.ViewHolder
                    holder.button.setColorFilter(ContextCompat.getColor(this@DoEditEventFragment.requireContext(), R.color.purple_700), PorterDuff.Mode.SRC_ATOP)
                    holder.button.setImageResource(event.icon.id)
                }
                if(!event.goodTimeRange.isEmptyTimeRange())
                    arrayOf(
                        R.id.add_even_goodtime_start_button to event.goodTimeRange.start,
                        R.id.add_even_goodtime_end_button to event.goodTimeRange.end,
                    ).forEach { (buttonId, time) ->
                        view.findViewById<Button>(buttonId).text = time
                    }
                if(!event.badTimeRange.isEmptyTimeRange())
                    arrayOf(
                        R.id.add_even_badtime_start_button to event.badTimeRange.start,
                        R.id.add_even_badtime_end_button to event.badTimeRange.end,
                    ).forEach { (buttonId, time) ->
                        view.findViewById<Button>(buttonId).text = time
                    }

                currentSelectedIcon = event.icon

                // set callback
                view.findViewById<Button>(R.id.add_event_button).setOnClickListener {
                    onSubmission(event)
                }
            }
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
}