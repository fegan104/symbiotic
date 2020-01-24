package com.frankegan.symbiotic.data.units

inline class Kilogram(val amount: Double) {
    override fun toString() = "$amount kg"

    operator fun plus(increment: Kilogram) = Kilogram(this.amount + increment.amount)

    operator fun minus(decrement: Kilogram) = Kilogram(this.amount - decrement.amount)
}

val Double.kg
    get() = Kilogram(this)

val Int.kg
    get() = Kilogram(this.toDouble())

val Double.lbs
    get() = Kilogram(this * 0.453592)

val Int.lbs
    get() = Kilogram(this * 0.453592)

val Double.oz
    get() = Kilogram(this * 0.0283495)

val Int.oz
    get() = Kilogram(this * 0.0283495)