package com.frankegan.symbiotic.data.local

import com.frankegan.symbiotic.data.*

class SymbioticLocalDataSource(private val database: SymbioticDatabase) : SymbioticDataSource {

    override suspend fun getFermentation(id: String) = try {
        val result = database.fermentationDao().selectById(id)
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun saveFermentation(
        fermentation: Fermentation,
        ingredients: List<Ingredient>,
        images: List<Image>,
        note: Note?
    ) = try {
        database.apply {
            if (fermentationDao().insert(fermentation) <= 0) throw (LocalDataNotFoundException())
            note?.let {
                if (noteDao().insert(it) <= 0) throw (LocalDataNotFoundException())
            }
            if (ingredients.isNotEmpty() && ingredientDao().insertAll(ingredients).any { it <= 0 }) {
                throw (LocalDataNotFoundException())
            }
            if (images.isNotEmpty() && imageDao().insertAll(images).any { it <= 0 }) {
                throw (LocalDataNotFoundException())
            }
        }
        Result.Success(fermentation)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun deleteFermentation(id: String) = try {
        val count = database.fermentationDao().delete(id)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(id)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun getFermentations() = try {
        val result = database.fermentationDao().selectAll()
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun getIngredients(fermentationId: String) = try {
        val result = database.ingredientDao().selectByFermentation(fermentationId)
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun updateIngredient(ingredient: Ingredient) = try {
        val count = database.ingredientDao().update(ingredient)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(ingredient)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun deleteIngredient(id: String) = try {
        val count = database.ingredientDao().delete(id)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(id)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun getImages(fermentationId: String) = try {
        val result = database.imageDao().selectByFermentation(fermentationId)
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun deleteImage(filename: String) = try {
        val count = database.imageDao().delete(filename)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(filename)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun getNote(fermentationId: String) = try {
        val result = database.noteDao().selectByFermentation(fermentationId)
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun deleteNote(id: String) = try {
        val count = database.noteDao().delete(id)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(id)
    } catch (exception: Exception) {
        Result.Error(exception)
    }
}