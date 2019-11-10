package com.frankegan.symbiotic.data.local

import androidx.room.*
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.data.Image
import com.frankegan.symbiotic.data.Ingredient
import com.frankegan.symbiotic.data.Note

@Dao
interface FermentationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fermentation: Fermentation): Long

    @Query("SELECT * FROM fermentation WHERE id = :id LIMIT 1")
    suspend fun selectById(id: String): Fermentation

    @Query("SELECT * FROM fermentation")
    suspend fun selectAll(): List<Fermentation>

    @Query("DELETE FROM fermentation WHERE id = :id")
    suspend fun delete(id: String): Int

    @Update
    suspend fun update(fermentation: Fermentation): Int
}

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image): Long

    @Query("SELECT * FROM image WHERE fermentation = :fermentationId")
    suspend fun selectByFermentation(fermentationId: String): List<Image>

    @Query("DELETE FROM image WHERE fileUri = :fileUri")
    suspend fun delete(fileUri: String): Int
}

@Dao
interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredient: List<Ingredient>): List<Long>

    @Query("SELECT * FROM ingredient WHERE fermentation = :fermentationId")
    suspend fun selectByFermentation(fermentationId: String): List<Ingredient>

    @Query("DELETE FROM ingredient WHERE id = :id")
    suspend fun delete(id: String): Int

    @Update
    suspend fun update(ingredient: Ingredient): Int
}

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Query("SELECT * FROM note WHERE fermentation = :fermentationId")
    suspend fun selectByFermentation(fermentationId: String): List<Note>

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun delete(id: String): Int
}