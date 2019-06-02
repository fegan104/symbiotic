package com.frankegan.symbiotic.data.local

import com.frankegan.symbiotic.data.*

class LocalDataSource(private val database: SymbioticDatabase) : SymbioticDataSource {

    override suspend fun getFermentation(id: String) = try {
        val result = database.fermentationDao().selectById(id)
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }


    override suspend fun createFermentation(fermentation: Fermentation) = try {
        val count = database.fermentationDao().insert(fermentation)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(fermentation)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun updateFermentation(fermentation: Fermentation) = try {
        val count = database.fermentationDao().update(fermentation)
        if (count <= 0) throw (LocalDataNotFoundException())
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

    override suspend fun createIngredients(ingredients: List<Ingredient>) = try {
        val count = database.ingredientDao().insertAll(ingredients)
        if (count.isEmpty()) throw (LocalDataNotFoundException())
        Result.Success(ingredients)
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

    override suspend fun createImage(image: Image) = try {
        val count = database.imageDao().insert(image)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(image)
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
        val result = database.noteDao().selectByFermentation(fermentationId).first()
        Result.Success(result)
    } catch (exception: Exception) {
        Result.Error(exception)
    }

    override suspend fun createNote(note: Note) = try {
        val count = database.noteDao().insert(note)
        if (count <= 0) throw (LocalDataNotFoundException())
        Result.Success(note)
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