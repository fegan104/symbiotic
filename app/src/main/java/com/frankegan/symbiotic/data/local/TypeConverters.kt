package com.frankegan.symbiotic.data.local

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime

class DateConverter {

    @TypeConverter
    fun toDate(dateString: String) = LocalDateTime.parse(dateString)

    @TypeConverter
    fun toTimestamp(date: LocalDateTime) = date.toString()
}