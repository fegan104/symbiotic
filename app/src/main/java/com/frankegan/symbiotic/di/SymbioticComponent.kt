package com.frankegan.symbiotic.di

import com.frankegan.symbiotic.data.SymbioticRepository
import com.frankegan.symbiotic.ui.addedit.AddEditViewModel
import com.frankegan.symbiotic.ui.home.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface SymbioticComponent {

    fun symbioticRepository(): SymbioticRepository

    fun homeViewModelFactory(): VMInjectionFactory<HomeViewModel>

    fun addEditViewModelFactory(): VMInjectionFactory<AddEditViewModel>
}