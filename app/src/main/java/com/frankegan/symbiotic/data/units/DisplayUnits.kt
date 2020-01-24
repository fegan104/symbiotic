package com.frankegan.symbiotic.data.units

import android.os.Parcel
import kotlinx.android.parcel.Parceler

interface DisplayUnit {
    companion object {
        fun valueOf(value: String): DisplayUnit {
            return try {
                MassDisplayUnit.valueOf(value)
            } catch (e: IllegalArgumentException) {
                try {
                    VolumeDisplayUnit.valueOf(value)
                } catch (e: IllegalArgumentException) {
                    try {
                        MassDisplayUnit.valueOf(value)
                    } catch (e: IllegalArgumentException) {
                        GeneralDisplayUnit.valueOf(value)
                    }
                }
            }
        }
    }
}

fun DisplayUnit.enumName(): String {
    return when (this) {
        is MassDisplayUnit -> this.name
        is VolumeDisplayUnit -> this.name
        is GeneralDisplayUnit -> this.name
        else -> throw Exception("Couldn't find DisplayUnit name")
    }
}

fun DisplayUnit.label(quantity: Double): String {
    return "$quantity ${enumName()}"
}

enum class MassDisplayUnit(val kilograms: Kilogram) : DisplayUnit {
    Lbs(1.lbs),
    Ounce(1.oz)
}

enum class VolumeDisplayUnit(val liters: Liter) : DisplayUnit {
    Gallon(1.gallon),
    Cup(1.cup),
    Tablespoon(1.tbsp)
}

enum class GeneralDisplayUnit : DisplayUnit {
    Slice,
    Each,
    Package
}

object DisplayUnitParceler : Parceler<DisplayUnit> {
    override fun create(parcel: Parcel): DisplayUnit {
        val enumKey = parcel.readString()!!
        return DisplayUnit.valueOf(enumKey)
    }

    override fun DisplayUnit.write(parcel: Parcel, flags: Int) {
        parcel.writeString(this.enumName())
    }
}