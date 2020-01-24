package com.frankegan.symbiotic.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.frankegan.symbiotic.data.units.DisplayUnit
import com.frankegan.symbiotic.data.units.DisplayUnitParceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity
data class Fermentation(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val headerUrl: String,
    @TypeConverters
    val startDate: LocalDateTime,
    @TypeConverters
    val firstEndDate: LocalDateTime,
    @TypeConverters
    val secondEndDate: LocalDateTime
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Fermentation::class,
            parentColumns = ["id"],
            childColumns = ["fermentation"],
            onDelete = CASCADE
        )
    ]
)
@Parcelize
data class Image(
    @PrimaryKey val fileUri: String,
    val caption: String,
    val fermentation: String
) : Parcelable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Fermentation::class,
            parentColumns = ["id"],
            childColumns = ["fermentation"],
            onDelete = CASCADE
        )
    ]
)
@Parcelize
@TypeParceler<DisplayUnit, DisplayUnitParceler>
data class Ingredient(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Double,
    @TypeConverters val unit: DisplayUnit,
    val fermentation: String
) : Parcelable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Fermentation::class,
            parentColumns = ["id"],
            childColumns = ["fermentation"],
            onDelete = CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val content: String,
    val fermentation: String
)

open class DataSourceException(message: String? = null) : Exception(message)

class LocalDataNotFoundException : DataSourceException("Data not found in local data source")
