package com.team6.connectbca.extensions

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCurrentDateString() : String {
    val date = getCurrentDate()
    val dateString = date.toString("dd-MM-yyyy")

    return dateString
}

fun getCurrentDate() : Date {
    return Calendar.getInstance().time
}

fun getEndOfMonthDate() : String {
    val date = getEndDate()
    val month = getMonth()
    val year = getYear()

    return "$date-$month-$year"
}

fun getFirstOfMonthDate() : String {
    val month = getMonth()
    val year = getYear()

    return "01-$month-$year"
}

fun milisecondToDateMonth(milisecond: Long, format: String) : String {
    val date = Date(milisecond)
    val formatter = SimpleDateFormat(format, Locale.getDefault())

    return formatter.format(date)
}

fun getNonZeroMonth() : String {
    val date = getCurrentDate()
    val month = date.toString("M")

    return month
}

fun reformatDate(date: String, sourceFormat: String, format: String) : String {
    val sourceFormatter = SimpleDateFormat(sourceFormat, Locale.getDefault())
    val converted = sourceFormatter.parse(date)
    val resultFormatter = SimpleDateFormat(format, Locale.getDefault())

    return resultFormatter.format(converted)
}

private fun getEndDate() : String {
    val date = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

    return date.toString()
}

private fun getMonth() : String {
    val date = getCurrentDate()
    val month = date.toString("MM")

    return month
}

fun getYear() : String {
    val date = getCurrentDate()
    val year = date.toString("yyyy")

    return year
}

private fun Date.toString(format: String, locale: Locale = Locale.getDefault()) : String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}