package com.example.todo.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDoViewModel
import com.example.todo.data.Priority
import com.example.todo.data.TaskData
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.row_layout.view.*

class TaskListAdapter: RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    var data = emptyList<TaskData>()

    inner class ViewHolder(private val v: View): RecyclerView.ViewHolder(v) {

        fun bind(item: TaskData) {
            v.title_text.text = item.title
            when(item.priority) {
                Priority.HIGH -> v.priority_icon.setCardBackgroundColor(ContextCompat.getColor(v.context,
                    R.color.colorRed
                ))
                Priority.MEDIUM -> v.priority_icon.setCardBackgroundColor(ContextCompat.getColor(v.context,
                    R.color.colorYellow
                ))
                Priority.LOW -> v.priority_icon.setCardBackgroundColor(ContextCompat.getColor(v.context,
                    R.color.colorAccent
                ))
            }
            v.row_background.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
                v.findNavController().navigate(action)
            }
            v.check.setOnClickListener {
                item.done = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    internal fun setData(data: List<TaskData>) {
        this.data = data
        notifyDataSetChanged()
    }
}