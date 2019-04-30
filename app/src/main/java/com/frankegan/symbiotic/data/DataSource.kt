package com.frankegan.symbiotic.data

interface DataSource {
    fun getFermentation(): Fermentation
    fun getFermentations(): List<Fermentation>

    fun getIngredients(fermentation: Fermentation): List<Ingredient>
    fun getImages(fermentation: Fermentation): List<Image>
    fun getNotes(fermentation: Fermentation): List<Note>
}