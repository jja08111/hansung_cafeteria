package com.foundy.hansungcafeteria.util

import android.annotation.SuppressLint
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun getMonToFriDateString(): String {
    val now = LocalDate.now()
    val formatter = SimpleDateFormat("M/dd")
    val monday = now.withDayOfWeek(DateTimeConstants.MONDAY).toDate()
    val friday = now.withDayOfWeek(DateTimeConstants.FRIDAY).toDate()

    return "${formatter.format(monday)} - ${formatter.format(friday)}"
}