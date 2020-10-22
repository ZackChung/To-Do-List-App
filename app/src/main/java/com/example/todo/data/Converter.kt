package com.example.todo.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(date: String?): LocalDate? {
        return if(date != null) LocalDate.parse(date, DateTimeFormatter.ISO_DATE) else null
    }
}