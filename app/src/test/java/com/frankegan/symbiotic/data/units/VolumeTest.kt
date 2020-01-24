package com.frankegan.symbiotic.data.units

import org.junit.Assert.assertEquals
import org.junit.Test

class VolumeTest {

    @Test
    fun `tbsp to Liter`() {
        assertEquals(6.1.tbsp, 3.tbsp + 3.1.tbsp)
        assertEquals(10.tbsp, 5.tbsp + 5.tbsp)
        assertEquals(10.tbsp, 15.tbsp - 5.tbsp)
    }
}