package com.frankegan.symbiotic.di

import android.app.Activity


interface DaggerComponentProvider {
    val component: SymbioticComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component