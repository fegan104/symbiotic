package com.frankegan.symbiotic.ui.addedit

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.frankegan.symbiotic.data.*
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.notifications.NotificationWorker
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class AddEditViewModel @Inject constructor(
    private val app: Application,
    private val symbioticRepo: SymbioticRepository
) : AndroidViewModel(app) {

    private val _fermentationData = MutableLiveData<Fermentation?>()
    val fermentationData: LiveData<Fermentation?>
        get() = _fermentationData

    private val _ingredientData = MutableLiveData<List<Ingredient>>().apply { value = emptyList() }
    val ingredientData: LiveData<List<Ingredient>>
        get() = _ingredientData

    private val _imageData = MutableLiveData<List<Image>>().apply { value = emptyList() }
    val imageData: LiveData<List<Image>>
        get() = _imageData

    private val _noteData = MutableLiveData<Note?>()
    val noteData: LiveData<Note?>
        get() = _noteData

    fun loadFermentationData(id: String?) {
        if (id == null) {
            _fermentationData.value = Fermentation(
                title = "",
                startDate = LocalDateTime.now(),
                firstEndDate = LocalDateTime.now().plusDays(10),
                secondEndDate = LocalDateTime.now().plusDays(14)
            )
            _imageData.value = emptyList()
            _ingredientData.value = emptyList()
            _noteData.value = null
            return
        }
        viewModelScope.launch {
            _fermentationData.value = when (val result = symbioticRepo.getFermentation(id)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _ingredientData.value = when (val result = symbioticRepo.getIngredients(id)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _imageData.value = when (val result = symbioticRepo.getImages(id)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
            _noteData.value = when (val result = symbioticRepo.getNote(id)) {
                is Result.Success -> result.data
                is Result.Error -> null
            }
        }
    }

    /**
     * Persist data and schedule reminder notifications for the new fermentation.
     */
    fun saveFermentation() = viewModelScope.launchSilent {
        val fermentation = fermentationData.value ?: return@launchSilent
        symbioticRepo.createFermentation(fermentation)
        _fermentationData.value = null

        val ingredients = ingredientData.value
        symbioticRepo.createIngredients(ingredients ?: emptyList())
        _ingredientData.value = emptyList()

        val images = imageData.value
        images?.forEach { symbioticRepo.createImage(it) }
        _imageData.value = emptyList()

        val note = noteData.value ?: return@launchSilent
        symbioticRepo.createNote(note)
        _noteData.value = null

        NotificationWorker.enqueueWork(app, fermentation)
    }

    fun addDetails(
        name: String = "",
        start: String = "",
        first: String = "",
        second: String = ""
    ) {
        val fermentation = fermentationData.value ?: return
        _fermentationData.value = fermentation.copy(
            title = if (name.isNotBlank()) name else fermentation.title,
            startDate = if (start.isNotBlank()) LocalDateTime.parse(
                start, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT)
            ) else fermentation.startDate,
            firstEndDate = if (first.isNotBlank()) LocalDateTime.parse(
                first, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT)
            ) else fermentation.firstEndDate,
            secondEndDate = if (second.isNotBlank()) LocalDateTime.parse(
                second, DateTimeFormatter.ofPattern(DETAIL_DATE_FORMAT)
            ) else fermentation.secondEndDate
        )
    }

    fun addIngredient(name: String, quantity: Double, unit: String) {
        val fermentationId = fermentationData.value?.id ?: return
        val ingredient = Ingredient(
            name = name,
            quantity = quantity,
            unit = unit,
            fermentation = fermentationId
        )
        _ingredientData.value = ingredientData.value!! + ingredient
    }

    fun addImage(fileUri: Uri?, caption: String = "") {
        val fermentationId = fermentationData.value?.id ?: return
        val image = Image(
            fileUri = fileUri?.toString() ?: return,
            caption = caption,
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
        _imageData.value = imageData.value!!.map {
            if (it.fileUri == filename) it.copy(caption = caption) else it
        }
    }

    fun addNote(content: String) {
        val fermentation = fermentationData.value ?: return
        val note = noteData.value ?: Note(content = content, fermentation = fermentation.id)
        _noteData.value = note.copy(content = content)
    }

    /**
     * Removes the fermentation and related data and cancels any pending reminders about the fermentation.
     */
    fun deleteFermentation() = viewModelScope.launchSilent {
        val fermentation = fermentationData.value ?: return@launchSilent
        NotificationWorker.cancelWork(app, fermentation)
        symbioticRepo.deleteFermentation(fermentation.id)
        imageData.value?.forEach { symbioticRepo.deleteImage(it.fileUri) }
        ingredientData.value?.forEach { symbioticRepo.deleteIngredient(it.id) }
        symbioticRepo.deleteNote(noteData.value?.id ?: return@launchSilent)
    }

    fun deleteIngredient(ingredient: Ingredient) {
        _ingredientData.value = ingredientData.value?.filter { it != ingredient }
    }
}
