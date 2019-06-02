package com.frankegan.symbiotic

import android.app.Application
import com.frankegan.symbiotic.di.AppModule
import com.frankegan.symbiotic.di.DaggerComponentProvider
import com.frankegan.symbiotic.di.DaggerSymbioticComponent
import com.frankegan.symbiotic.di.SymbioticComponent
import com.frankegan.symbiotic.notifications.createReminderChannel
import com.jakewharton.threetenabp.AndroidThreeTen

class SymbioticApp : Application(), DaggerComponentProvider {

    override val component: SymbioticComponent by lazy {
        DaggerSymbioticComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        createReminderChannel(this)
    }
}