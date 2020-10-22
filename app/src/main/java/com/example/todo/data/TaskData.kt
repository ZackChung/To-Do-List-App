package com.example.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo.data.Priority
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

@Parcelize
@Entity(tableName = "task_table")
data class TaskData(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var addToToday: Boolean,
    var priority: Priority,
    var dueDate: LocalDate?,
    var description: String?,
    var done: Boolean): Parcelable{
}