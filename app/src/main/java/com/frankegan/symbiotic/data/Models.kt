package com.frankegan.symbiotic.data

import org.threeten.bp.LocalDate

data class Fermentation(
    val id: Long,
    val title: String,
    val startDate: LocalDate,
    val firstEndDate: LocalDate,
    val secondEndDate: LocalDate
)

data class Image(
    val path: String,
    val caption: String,
    val fermentation: Long
)

data class Ingredient(
    val name: String,
    val unit: Unit,
    val quantity: Double,
    val fermentation: Long
)

data class Note(
    val content: String,
    val fermentation: Long
)

sealed class Unit {
    enum class Volume(val label: String) {
        OUNCE("oz"),
        CUP("cup"),
        GALLON("gal"),
        TEABAG("tbag"),
        TBSP("tbsp")
    }

    enum class Mass(val label: String) {
        KG("kg"),
        GRAM("gram"),
        OUNCE("oz"),
        LBS("lbs")
    }
}
