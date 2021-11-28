package com.example.sleepingkirby.definetask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.sleepingkirby.R

class DefineTaskFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_define_task, container, false)

        // set actions for navigation buttons
       view.findViewById<Button>(R.id.button_add_task)?.setOnClickListener {
            findNavController().navigate(R.id.action_defineTaskFragment_to_addTaskFragment)
        }
        view.findViewById<Button>(R.id.button_delete_task)?.setOnClickListener {
            findNavController().navigate(R.id.action_defineTaskFragment_to_removeTaskFragment)
        }
        view.findViewById<Button>(R.id.button_edit_task)?.setOnClickListener {
            findNavController().navigate(R.id.action_defineTaskFragment_to_editTaskFragment)
        }

        return view
    }
}