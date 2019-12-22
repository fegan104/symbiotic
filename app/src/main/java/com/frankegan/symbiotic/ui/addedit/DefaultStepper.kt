package com.frankegan.symbiotic.ui.addedit

import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

/**
 * A simple no-op [Step]
 */
object DefaultStepper : Step {
    override fun onSelected() = Unit
    override fun verifyStep(): VerificationError? = null
    override fun onError(error: VerificationError) = Unit
}