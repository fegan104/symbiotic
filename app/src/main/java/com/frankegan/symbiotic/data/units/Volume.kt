package com.frankegan.symbiotic.data.units

inline class Liter(val amount: Double) {

    override fun toString() = "$amount ltr"

    operator fun plus(increment: Liter) = Liter(this.amount + increment.amount)

    operator fun minus(decrement: Liter) = Liter(this.amount - decrement.amount)
}

val Double.ltr
    get() = Liter(this)

val Int.ltr
    get() = Liter(this.toDouble())

val Double.tbsp
    get() = Liter(this * 0.014787)

val Int.tbsp
    get() = Liter(this * 0.014787)

val Double.cup
    get() = Liter(this * 0.2365882365)

val Int.cup
    get() = Liter(this * 0.2365882365)

val Double.gallon
    get() = Liter(this * 4.54609)

val Int.gallon
    get() = Liter(this * 4.54609)

val Double.tsp
    get() = Liter(this * 0.0049289215940186)

val Int.tsp
    get() = Liter(this * 0.0049289215940186)