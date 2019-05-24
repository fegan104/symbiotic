package com.frankegan.symbiotic.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.data.Result
import com.frankegan.symbiotic.data.SymbioticRepository
import com.frankegan.symbiotic.launchSilent
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val symbioticRepo: SymbioticRepository
) : ViewModel() {
    /**
     * private mutable state for fermentation data.
     */
    private val fermentations = MutableLiveData<List<Fermentation>>()

    /**
     * Read only data to be observed. Calling this method always causes it's backing LiveData to be refreshed
     */
    fun fermentations(): LiveData<List<Fermentation>> = fermentations.apply { loadFermentation() }

    private fun loadFermentation() = viewModelScope.launchSilent {
        when (val result = symbioticRepo.getFermentations()) {
            is Result.Success -> {
                fermentations.value = result.data
            }
            is Result.Error -> result.exception.also { Log.e("HomeViewModel", it.message) }
        }
    }

    fun foo() = Unit
}