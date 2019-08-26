package com.frankegan.symbiotic.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.frankegan.symbiotic.data.Result
import com.frankegan.symbiotic.data.SymbioticRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val symbioticRepo: SymbioticRepository,
    app: Application
) : AndroidViewModel(app) {

    fun fermentationData() = liveData(Dispatchers.IO) {
        when (val result = symbioticRepo.getFermentations()) {
            is Result.Success -> {
                emit(result.data)
            }
            is Result.Error -> result.exception.also { Log.e("HomeViewModel", it.message) }
        }
    }
}