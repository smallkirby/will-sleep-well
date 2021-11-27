package com.example.sleepingkirby.defineevent.edit

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
import com.example.sleepingkirby.database.dailyevent.DailyEvent
import com.example.sleepingkirby.database.dailyevent.DailyEventViewModel
import com.example.sleepingkirby.database.dailyevent.DailyEventViewModelFactory
import com.orhanobut.logger.Logger
import kotlinx.coroutines.InternalCoroutinesApi

class EditEventFragment : Fragment() {

    @InternalCoroutinesApi
    private val dailyEventViewModel: DailyEventViewModel by viewModels {
        DailyEventViewModelFactory((activity?.application as SleepingApplication).eventRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_event, container, false)

        return view
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DailyEventListEditAdapter {
            onItemClicked(it)
        }

        view.findViewById<RecyclerView>(R.id.edit_events_recycler).apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this.context)
        }

        dailyEventViewModel.allDailyEvents.observe(viewLifecycleOwner, { events ->
            events?.let { adapter.submitList(it) }
        })
    }

    private fun onItemClicked(event: DailyEvent) {
        val action = EditEventFragmentDirections.actionEditEventFragmentToDoEditEventFragment(event.id)
        view?.findNavController()?.navigate(action)
    }
}