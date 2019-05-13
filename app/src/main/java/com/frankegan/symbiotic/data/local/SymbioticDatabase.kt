package com.frankegan.symbiotic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.data.Image
import com.frankegan.symbiotic.data.Ingredient
import com.frankegan.symbiotic.data.Note

@Database(
    version = 1,
    entities = [
        Fermentation::class,
        Ingredient::class,
        Image::class,
        Note::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class SymbioticDatabase : RoomDatabase() {
    abstract fun fermentationDao(): FermentationDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun imageDao(): ImageDao
    abstract fun noteDao(): NoteDao
}