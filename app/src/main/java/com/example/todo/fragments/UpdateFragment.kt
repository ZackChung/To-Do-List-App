package com.example.todo.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.ToDoViewModel
import com.example.todo.data.Priority
import com.example.todo.data.TaskData
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.time.LocalDate

class UpdateFragment : Fragment() {

    private val args: UpdateFragmentArgs by navArgs()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_update, container, false)

        view.current_title.setText(args.currentTask.title)
        view.current_checkbox.isChecked = args.currentTask.addToToday
        view.current_priority.setSelection(parsePriorityToInt(args.currentTask.priority))
        view.current_due_date.setSelection(parseDueDateToInt(args.currentTask.dueDate))
        view.current_description.setText(args.currentTask.description)

        view.current_priority.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
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
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateTask()
            R.id.menu_delete -> deleteTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTask() {
        val currentTitle = current_title.text.toString()
        val currentAddToToday = current_checkbox.isChecked
        val currentPriority = current_priority.selectedItem.toString()
        val currentDue = current_due_date.selectedItem.toString()
        val currentDescription = if(current_description.text.isNotEmpty()) current_description.text.toString() else null

        if(currentTitle.isNotEmpty()) {
            val updateTask = TaskData(
                args.currentTask.id,
                currentTitle,
                currentAddToToday,
                parsePriority(currentPriority),
                parseDate(currentDue),
                currentDescription,
                args.currentTask.done
            )
            toDoViewModel.updateTask(updateTask)
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(context, "Please enter your task title.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTask() {
        val alert = AlertDialog.Builder(context)
        alert.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteTask(args.currentTask)
            Toast.makeText(context, "Successfully deleted \"${args.currentTask.title}\"!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        alert.setNegativeButton("No") {_, _ -> }
        alert.setTitle("Delete \"${args.currentTask.title}\" Task?")
        alert.setMessage("Are you sure you want to delete \"${args.currentTask.title}\" task?")
        alert.create().show()
    }

    private fun parsePriorityToInt(priority: Priority): Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    private fun parseDueDateToInt(due: LocalDate?): Int {
        return when(due) {
            LocalDate.now() -> 0
            LocalDate.now().plusDays(1) -> 1
            else -> 2
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