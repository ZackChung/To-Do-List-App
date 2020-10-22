package com.example.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskData
import com.example.todo.data.ToDoDatabase
import com.example.todo.data.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ToDoRepository
    val allTasks: LiveData<List<TaskData>>
    val todayTasks: LiveData<List<TaskData>>
    val plannedTasks: LiveData<List<TaskData>>
    val doneTasks: LiveData<List<TaskData>>

    val sortByHighPriority: LiveData<List<TaskData>>
    val sortByLowPriority: LiveData<List<TaskData>>
    val sortByCreatedDateAsc: LiveData<List<TaskData>>
    val sortByCreatedDateDesc: LiveData<List<TaskData>>

    init {
        val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
        repository = ToDoRepository(toDoDao)
        allTasks = repository.allTasks
        todayTasks = repository.todayTasks
        plannedTasks = repository.plannedTasks
        doneTasks = repository.doneTasks

        sortByHighPriority = repository.sortByHighPriority
        sortByLowPriority = repository.sortByLowPriority
        sortByCreatedDateAsc = repository.sortByCreatedDateAsc
        sortByCreatedDateDesc = repository.sortByCreatedDateDesc
    }

    fun insertTask(taskData: TaskData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(taskData)
    }

    fun updateTask(taskData: TaskData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateData(taskData)
    }

    fun deleteTask(taskData: TaskData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteTask(taskData)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun search(searchString: String): LiveData<List<TaskData>> {
        return repository.search(searchString)
    }
}