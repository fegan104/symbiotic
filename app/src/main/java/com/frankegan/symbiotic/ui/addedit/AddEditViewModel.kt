package com.frankegan.symbiotic.ui.addedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frankegan.symbiotic.data.*
import com.frankegan.symbiotic.launchSilent
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class AddEditViewModel @Inject constructor(
    private val symbioticRepo: SymbioticRepository
) : ViewModel() {

    private val _fermentationData = MutableLiveData<Fermentation>()
    val fermentationData: LiveData<Fermentation> = _fermentationData

    private val _ingredientData = MutableLiveData<List<Ingredient>>().apply { value = emptyList() }
    val ingredientData: LiveData<List<Ingredient>> = _ingredientData

    private val _imageData = MutableLiveData<List<Image>>().apply { value = emptyList() }
    val imageData: LiveData<List<Image>> = _imageData

    private val _noteData = MutableLiveData<Note>()
    val noteDate: LiveData<Note> = _noteData

    fun loadFermentationData(id: String?) = viewModelScope.launchSilent {
        if (id == null) {
            _fermentationData.value = Fermentation(
                title = "",
                startDate = LocalDateTime.now(),
                firstEndDate = LocalDateTime.now().plusDays(10),
                secondEndDate = LocalDateTime.now().plusDays(14)
            )
            return@launchSilent
        }
        _fermentationData.value = when (val result = symbioticRepo.getFermentation(id)) {
            is Result.Success -> result.data
            is Result.Error -> null
        }
    }

    fun saveFermentation(fermentation: Fermentation) = viewModelScope.launchSilent { }

    fun addDetails(
        name: String = "",
        start: String = "",
        first: String = "",
        second: String = ""
    ) {
        val fermentation = fermentationData.value ?: return
        _fermentationData.value = fermentation.copy(
            title = if (name.isBlank()) name else fermentation.title,
            startDate = if (start.isBlank()) LocalDateTime.parse(start) else fermentation.startDate,
            firstEndDate = if (first.isBlank()) LocalDateTime.parse(first) else fermentation.firstEndDate,
            secondEndDate = if (second.isBlank()) LocalDateTime.parse(second) else fermentation.secondEndDate
        )
    }

    fun addIngredient(name: String, quantity: Double, unit: String) {
        val fermentationId = _fermentationData.value?.id ?: return
        val ingredient = Ingredient(
            name = name,
            quantity = quantity,
            unit = unit,
            fermentation = fermentationId
        )
        _ingredientData.value = listOf(ingredient, *ingredientData.value!!.toTypedArray())
    }

    fun addImage(filename: String) {
        val fermentationId = _fermentationData.value?.id ?: return
        val image = Image(
            filename = filename,
            caption = "",
            fermentation = fermentationId
        )
        _imageData.value = listOf(image, *imageData.value!!.toTypedArray())
    }

    /**
     * Change the caption of the selected [Image]. This is done in memory with the understanding it will be persisted
     * upon the user selecting to save the fermentation later.
     *
     * @param filename The key for updating an [Image] record.
     * @param caption The new caption to add to an [Image] record.
     */
    fun addCaption(filename: String, caption: String) {
        _imageData.value = imageData.value!!.map { if (it.filename == filename) it.copy(caption = caption) else it }
    }
}
