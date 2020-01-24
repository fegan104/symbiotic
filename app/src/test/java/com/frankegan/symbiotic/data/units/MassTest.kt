package com.frankegan.symbiotic.data.units

import org.junit.Assert.assertEquals
import org.junit.Test

class MassTest {

    @Test
    fun `lbs to kg`() {
        assertEquals(5.kg, 2.kg + 3.kg)
        assertEquals(2.26796.kg.amount, (2.lbs + 3.lbs).amount, 0.001)
        assertEquals(7.72155422.kg.amount, (6.lbs + 5.kg).amount, 0.001)
    }
}