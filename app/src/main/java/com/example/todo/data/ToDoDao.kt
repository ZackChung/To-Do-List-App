package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun getAllTasks(): LiveData<List<TaskData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(taskData: TaskData)

    @Update
    suspend fun updateData(taskData: TaskData)

    @Delete
    suspend fun deleteTask(taskData: TaskData)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table WHERE title LIKE :searchString")
    fun search(searchString: String): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun sortByCreatedDateAsc(): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table ORDER BY id")
    fun sortByCreatedDateDesc(): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table WHERE addToToday = :addToToday or dueDate = :due")
    fun getTodayTasks(addToToday: Boolean = true, due: String): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table WHERE addToToday = :addToToday or dueDate != :due")
    fun getPlannedTasks(addToToday: Boolean = false, due: String): LiveData<List<TaskData>>

    @Query("SELECT * FROM task_table WHERE done = :done")
    fun getDoneTasks(done: Boolean = true): LiveData<List<TaskData>>
}