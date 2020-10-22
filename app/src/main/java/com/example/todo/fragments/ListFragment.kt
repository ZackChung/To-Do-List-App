package com.example.todo.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDoViewModel
import com.example.todo.data.TaskData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val toDoViewModel: ToDoViewModel by viewModels()

    private val adapter = TaskListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val addButton = view.findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        val taskList = view.findViewById<RecyclerView>(R.id.taskList)
        taskList.adapter = adapter
        taskList.layoutManager = LinearLayoutManager(requireActivity())

        toDoViewModel.todayTasks.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        val today = view.findViewById<TextView>(R.id.today)
        today.setOnClickListener {
            toDoViewModel.todayTasks.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

        val all = view.findViewById<TextView>(R.id.all)
        all.setOnClickListener {
            toDoViewModel.allTasks.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

        val planned = view.findViewById<TextView>(R.id.planned)
        planned.setOnClickListener {
            toDoViewModel.plannedTasks.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

        val done = view.findViewById<TextView>(R.id.done)
        done.setOnClickListener {
            toDoViewModel.doneTasks.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }


        enableSwipeToDelete(taskList)

        setHasOptionsMenu(true)

        return  view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> deleteAll()
            R.id.menu_priority_high -> toDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> toDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_created_date_asc -> toDoViewModel.sortByCreatedDateAsc.observe(this, Observer { adapter.setData(it) })
            R.id.menu_created_date_desc -> toDoViewModel.sortByCreatedDateDesc.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchString = "%$query%"
        toDoViewModel.search(searchString).observe(this, Observer {
            adapter.setData(it)
        })
    }

    private fun deleteAll() {
        val alert = AlertDialog.Builder(context)
        alert.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteAll()
            Toast.makeText(context, "Successfully deleted everything!", Toast.LENGTH_SHORT).show()
        }
        alert.setNegativeButton("No") {_, _ -> }
        alert.setTitle("Delete  All?")
        alert.setMessage("Are you sure you want to delete everything?")
        alert.create().show()
    }

    private fun enableSwipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object: SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskToDelete = adapter.data[viewHolder.adapterPosition]
                toDoViewModel.deleteTask(taskToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                undoDelete(viewHolder.itemView, taskToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun undoDelete(view: View, deletedTask: TaskData, position: Int) {
        val snackBar = Snackbar.make(view, "Deleted \"${deletedTask.title}\" Task.", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            toDoViewModel.insertTask(deletedTask)
            adapter.notifyItemChanged(position)
        }
        snackBar.show()
    }

}