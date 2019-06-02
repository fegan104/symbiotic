package com.frankegan.symbiotic.data

interface SymbioticDataSource {
    suspend fun getFermentation(id: String): Result<Fermentation>
    suspend fun createFermentation(fermentation: Fermentation): Result<Fermentation>
    suspend fun updateFermentation(fermentation: Fermentation): Result<Fermentation>
    suspend fun deleteFermentation(id: String): Result<String>
    suspend fun getFermentations(): Result<List<Fermentation>>

    suspend fun getIngredients(fermentationId: String): Result<List<Ingredient>>
    suspend fun createIngredients(ingredients: List<Ingredient>): Result<List<Ingredient>>
    suspend fun updateIngredient(ingredient: Ingredient): Result<Ingredient>
    suspend fun deleteIngredient(id: String): Result<String>

    suspend fun createImage(image: Image): Result<Image>
    suspend fun getImages(fermentationId: String): Result<List<Image>>
    suspend fun deleteImage(filename: String): Result<String>

    suspend fun getNote(fermentationId: String): Result<Note>
    suspend fun createNote(note: Note): Result<Note>
    suspend fun deleteNote(id: String): Result<String>
}