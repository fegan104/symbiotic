package com.frankegan.symbiotic.data.local

import androidx.room.TypeConverter
import com.frankegan.symbiotic.data.units.DisplayUnit
import com.frankegan.symbiotic.data.units.enumName
import org.threeten.bp.LocalDateTime

class DateConverter {

    @TypeConverter
    fun toDate(dateString: String): LocalDateTime = LocalDateTime.parse(dateString)

    @TypeConverter
    fun toTimestamp(date: LocalDateTime) = date.toString()
}

class DisplayUnitConverter {

    @TypeConverter
    fun toDisplayUnit(unitString: String) = DisplayUnit.valueOf(unitString)

    @TypeConverter
    fun toDisplayUnitName(displayUnit: DisplayUnit) = displayUnit.enumName()
}