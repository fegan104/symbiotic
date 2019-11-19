package com.frankegan.symbiotic.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime
import java.util.*

@Entity
data class Fermentation(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
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
data class Ingredient(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Double,
    val unit: String,
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

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()
}

open class DataSourceException(message: String? = null) : Exception(message)

class LocalDataNotFoundException : DataSourceException("Data not found in local data source")
