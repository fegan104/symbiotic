package com.frankegan.symbiotic.di

import com.frankegan.symbiotic.notifications.NotificationWorker
import com.frankegan.symbiotic.ui.addedit.AddEditFragment
import com.frankegan.symbiotic.ui.addedit.DetailsFragment
import com.frankegan.symbiotic.ui.addedit.HeaderGalleryFragment
import com.frankegan.symbiotic.ui.addedit.IngredientsFragment
import com.frankegan.symbiotic.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface SymbioticComponent {

    fun inject(fragment: HomeFragment)

    fun inject(fragment: AddEditFragment)

    fun inject(fragment: DetailsFragment)

    fun inject(fragment: IngredientsFragment)

    fun inject(fragment: HeaderGalleryFragment)

    fun inject(worker: NotificationWorker)
}