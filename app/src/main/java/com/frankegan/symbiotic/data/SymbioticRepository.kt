package com.frankegan.symbiotic.data

import com.frankegan.symbiotic.data.local.LocalDataSource
import javax.inject.Inject

class SymbioticRepository @Inject constructor(
    private val localSource: LocalDataSource
) : SymbioticDataSource by localSource
