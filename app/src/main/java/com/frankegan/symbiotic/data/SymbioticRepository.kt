package com.frankegan.symbiotic.data

import com.frankegan.symbiotic.data.local.SymbioticLocalDataSource
import javax.inject.Inject

class SymbioticRepository @Inject constructor(
    private val localSource: SymbioticLocalDataSource
) : SymbioticDataSource by localSource
