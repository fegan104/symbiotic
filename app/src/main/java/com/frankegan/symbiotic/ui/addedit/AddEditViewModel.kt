package com.frankegan.symbiotic.ui.addedit

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.frankegan.symbiotic.data.*
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.notifications.NotificationWorker
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

data class AddEditUiModel(
    val fermentation: Fermentation,
    val ingredients: List<Ingredient>,
    val images: List<Image>,
    val note: Note?
)

private fun initState() = AddEditUiModel(
    fermentation = Fermentation(
        title = "",
        startDate = LocalDateTime.now(),
        firstEndDate = LocalDateTime.now().plusDays(10),
        secondEndDate = LocalDateTime.now().plusDays(14)
    ),
    ingredients = emptyList(),
    images = emptyList(),
    note = null
)

class AddEditViewModel @Inject constructor(
    private val app: Application,
    private val symbioticRepo: SymbioticRepository
) : AndroidViewModel(app) {

    private val uiModel = MutableLiveData<AddEditUiModel>(initState())

    val fermentationData: LiveData<Fermentation?> = uiModel.map { it.fermentation }

    val ingredientData: LiveData<List<Ingredient>> = uiModel.map { it.ingredients }

    val imageData: LiveData<List<Image>> = uiModel.map { it.images }

    val noteData: LiveData<Note?> = uiModel.map { it.note }

    fun loadFermentationData(fermentationId: String?) = viewModelScope.launch {
        if (fermentationId == null) {
            uiModel.value = initState()
        }
        val currentSate = uiModel.value!!
        val id = fermentationId ?: currentSate.fermentation.id

        val fermentation = when (val result = symbioticRepo.getFermentation(id)) {
            is Result.Success -> result.data
            is Result.Error -> currentSate.fermentation
        }

        val ingredients = when (val result = symbioticRepo.getIngredients(id)) {
            is Result.Success -> result.data
            is Result.Error -> currentSate.ingredients
        }

        val images = when (val result = symbioticRepo.getImages(id)) {
            is Result.Success -> result.data
            is Result.Error -> currentSate.images
        }
        val note = when (val result = symbioticRepo.getNote(id)) {
            is Result.Success -> result.data
            is Result.Error -> currentSate.note
        }

        uiModel.value = currentSate.copy(
            fermentation = fermentation,
            ingredients = ingredients,
            images = images,
            note = note
        )
    }

    /**
     * TODO how do you update fermentations?
     * Persist data and schedule reminder notifications for the new fermentation.
     */
    fun saveFermentation() = GlobalScope.launchSilent {
        val currentState = uiModel.value ?: return@launchSilent
        //saveFermentation will insert or update appropriately
        val result = symbioticRepo.saveFermentation(
            currentState.fermentation,
            currentState.ingredients,
            currentState.images,
            currentState.note
        )
        when (result) {
            is Result.Success -> NotificationWorker.enqueueWork(app, currentState.fermentation)
            is Result.Error -> Log.e("AddEditViewModel", result.exception.message, result.exception)
        }
    }

    fun addDetails(
        name: String = "",
        start: String = "",
        first: String = "",
        second: String = ""
    ) {
        val currentState = uiModel.value ?: return
        val currentFermentation = currentState.fermentation

        val nextName = if (name.isNotBlank()) name else currentFermentation.title

        val nextStart = if (start.isNotBlank()) {
            LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT))
        } else {
            currentFermentation.startDate
        }
        val nextFirstEnd = if (first.isNotBlank()) {
            LocalDateTime.parse(first, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT))
        } else {
            currentFermentation.firstEndDate
        }
        val nextSecondEnd = if (second.isNotBlank()) {
            LocalDateTime.parse(second, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT))
        } else {
            currentFermentation.secondEndDate
        }

        val nextFermentation = currentFermentation.copy(
            title = nextName,
            startDate = nextStart,
            firstEndDate = nextFirstEnd,
            secondEndDate = nextSecondEnd
        )

        uiModel.value = currentState.copy(fermentation = nextFermentation)
    }

    fun addIngredient(name: String, quantity: Double, unit: String) {
        val currentState = uiModel.value ?: return

        val next = Ingredient(
            name = name,
            quantity = quantity,
            unit = unit,
            fermentation = currentState.fermentation.id
        )
        uiModel.value = currentState.copy(
            ingredients = currentState.ingredients + next
        )
    }

    fun addImage(fileUri: Uri?, caption: String = "") {
        val currentState = uiModel.value ?: return

        val next = Image(
            fileUri = fileUri?.toString() ?: return,
            caption = caption,
            fermentation = currentState.fermentation.id
        )
        uiModel.value = currentState.copy(
            images = currentState.images + next
        )
    }

    /**
     * Change the caption of the selected [Image]. This is done in memory with the understanding it will be persisted
     * upon the user selecting to save the fermentation later.
     *
     * @param filename The key for updating an [Image] record.
     * @param caption The new caption to add to an [Image] record.
     */
    fun addCaption(filename: String, caption: String) {
        val currentState = uiModel.value ?: return

        uiModel.value = currentState.copy(
            images = currentState.images.map {
                if (it.fileUri == filename) it.copy(caption = caption) else it
            }
        )
    }

    fun addNote(content: String) {
        val currentState = uiModel.value ?: return

        val next = Note(
            content = content,
            fermentation = currentState.fermentation.id
        )
        uiModel.value = currentState.copy(note = next)
    }

    /**
     * Removes the fermentation and related data and cancels any pending reminders about the fermentation.
     */
    fun deleteFermentation() = viewModelScope.launchSilent {
        val currentState = uiModel.value ?: return@launchSilent
        NotificationWorker.cancelWork(app, currentState.fermentation)
        //this will cascade delete all children
        symbioticRepo.deleteFermentation(currentState.fermentation.id)
    }

    fun deleteIngredient(ingredient: Ingredient) = viewModelScope.launchSilent {
        val currentState = uiModel.value ?: return@launchSilent
        uiModel.value = currentState.copy(
            ingredients = currentState.ingredients.filter { it != ingredient }
        )
    }
}
