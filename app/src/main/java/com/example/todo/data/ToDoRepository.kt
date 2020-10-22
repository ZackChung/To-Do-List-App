package com.example.todo.data

import androidx.lifecycle.LiveData
import java.time.LocalDate

class ToDoRepository(private val toDoDao: ToDoDao) {

    val allTasks: LiveData<List<TaskData>> = toDoDao.getAllTasks()
    val todayTasks: LiveData<List<TaskData>> = toDoDao.getTodayTasks(due = LocalDate.now().toString())
    val plannedTasks: LiveData<List<TaskData>> = toDoDao.getPlannedTasks(due = LocalDate.now().toString())
    val doneTasks: LiveData<List<TaskData>> = toDoDao.getDoneTasks()

    val sortByHighPriority: LiveData<List<TaskData>> = toDoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TaskData>> = toDoDao.sortByLowPriority()
    val sortByCreatedDateAsc: LiveData<List<TaskData>> = toDoDao.sortByCreatedDateAsc()
    val sortByCreatedDateDesc: LiveData<List<TaskData>> = toDoDao.sortByCreatedDateDesc()

    suspend fun insertData(taskData: TaskData) {
        toDoDao.insertData(taskData)
    }

    suspend fun updateData(taskData: TaskData) {
        toDoDao.updateData(taskData)
    }

    suspend fun deleteTask(taskData: TaskData) {
        toDoDao.deleteTask(taskData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun search(searchString: String): LiveData<List<TaskData>> {
        return toDoDao.search(searchString)
    }

}