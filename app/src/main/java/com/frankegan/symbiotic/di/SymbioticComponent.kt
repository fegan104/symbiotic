package com.frankegan.symbiotic.di

import com.frankegan.symbiotic.data.SymbioticRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface SymbioticComponent {

    fun symbioticRepository(): SymbioticRepository
}