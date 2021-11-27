package com.example.sleepingkirby.defineevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.sleepingkirby.R

class DefineEventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_define_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_add_event).setOnClickListener {
            findNavController().navigate(R.id.action_defineEventFragment_to_addEventFragment)
        }
        view.findViewById<Button>(R.id.button_edit_event).setOnClickListener {
            findNavController().navigate(R.id.action_defineEventFragment_to_editEventFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DefineEventFragment().apply {
            }
    }
}