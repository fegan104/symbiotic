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
    val fermentationLiveData: LiveData<Fermentation> = _fermentationData

    private val _ingredientData = MutableLiveData<List<Ingredient>>().apply { value = emptyList() }
    val ingredientData: LiveData<List<Ingredient>> = _ingredientData

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

    fun addImage(image: Image) = viewModelScope.launchSilent { }
}
