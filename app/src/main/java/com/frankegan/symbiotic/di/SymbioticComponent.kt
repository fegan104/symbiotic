package com.frankegan.symbiotic.di

import com.frankegan.symbiotic.data.SymbioticRepository
import com.frankegan.symbiotic.notifications.NotificationWorker
import com.frankegan.symbiotic.ui.addedit.AddEditFragment
import com.frankegan.symbiotic.ui.addedit.DetailsFragment
import com.frankegan.symbiotic.ui.addedit.GalleryFragment
import com.frankegan.symbiotic.ui.addedit.IngredientsFragment
import com.frankegan.symbiotic.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface SymbioticComponent {

    fun symbioticRepository(): SymbioticRepository

    fun inject(fragment: HomeFragment)

    fun inject(fragment: GalleryFragment)

    fun inject(fragment: AddEditFragment)

    fun inject(fragment: DetailsFragment)

    fun inject(fragment: IngredientsFragment)

    fun inject(worker: NotificationWorker)
}