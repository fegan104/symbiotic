package com.frankegan.symbiotic.di

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.work.ListenableWorker


interface DaggerComponentProvider {
    val component: SymbioticComponent
}

val Activity.injector get() = (application as DaggerComponentProvider).component

val Fragment.injector get() = requireActivity().injector

val ListenableWorker.injector get() = (applicationContext as DaggerComponentProvider).component