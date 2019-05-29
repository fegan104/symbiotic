package com.frankegan.symbiotic.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity
data class Fermentation(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    @TypeConverters
    val startDate: LocalDateTime,
    @TypeConverters
    val firstEndDate: LocalDateTime,
    @TypeConverters
    val secondEndDate: LocalDateTime
)

@Entity
@Parcelize
data class Image(
    @PrimaryKey val filename: String,
    val caption: String,
    @ForeignKey(
        entity = Fermentation::class,
        parentColumns = ["id"],
        childColumns = ["fermentation"]
    ) val fermentation: String
) : Parcelable

@Entity
@Parcelize
data class Ingredient(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Double,
    val unit: String,
    @ForeignKey(
        entity = Fermentation::class,
        parentColumns = ["id"],
        childColumns = ["fermentation"]
    ) val fermentation: String
) : Parcelable

@Entity
data class Note(
    @PrimaryKey val id: String,
    val content: String,
    @ForeignKey(
        entity = Fermentation::class,
        parentColumns = ["id"],
        childColumns = ["fermentation"]
    ) val fermentation: String
)

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()
}

open class DataSourceException(message: String? = null) : Exception(message)

class LocalDataNotFoundException : DataSourceException("Data not found in local data source")
