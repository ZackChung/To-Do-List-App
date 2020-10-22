package com.example.todo

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun main() {
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//    val date = Calendar.getInstance().time
//    val formattedDate = dateFormat.format(date)
//    println(formattedDate)

    val current = LocalDate.now()
    val tomorrow = current.plusDays(1)

    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val formatted = current.format(formatter)

    val test = "2020-10-16"
    val date = LocalDate.parse(test, DateTimeFormatter.ISO_DATE)

    println("Current Date is: $formatted")
    println("$date\n$current")
    println("Tomorrow is $tomorrow")
}