package com.example.todo.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todo.data.Priority
import com.example.todo.R
import com.example.todo.ToDoViewModel
import com.example.todo.data.TaskData
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.time.LocalDate

class AddFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        view.priority.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) { }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2) {
                    0 -> {(p0?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))}
                    1 -> {(p0?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorYellow))}
                    2 -> {(p0?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))}
                }
            }
        }

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add) {
            insertTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertTask() {
        val title = title.text.toString()
        val addToToday = checkbox.isChecked
        val priority = priority.selectedItem.toString()
        val due = due_date.selectedItem.toString()
        val description = if(description.text.isNotEmpty()) description.text.toString() else null

        if(title.isNotEmpty()) {
            val newTask = TaskData(
                0,
                title,
                addToToday,
                parsePriority(priority),
                parseDate(due),
                description,
                false
            )
            toDoViewModel.insertTask(newTask)
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
        else {
            Toast.makeText(context, "Please enter your task title.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }

    private fun parseDate(due: String): LocalDate? {
        return when(due) {
            "Due Today" -> LocalDate.now()
            "Due Tomorrow" -> LocalDate.now().plusDays(1)
            "Scheduled" -> null
            else -> null
        }
    }
}