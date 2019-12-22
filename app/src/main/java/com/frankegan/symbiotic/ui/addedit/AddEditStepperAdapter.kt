package com.frankegan.symbiotic.ui.addedit

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import javax.inject.Inject


class AddEditStepperAdapter @Inject constructor(
    fm: FragmentManager, context: Context
) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step = when (position) {
        0 -> DetailsFragment.newInstance()
        1 -> IngredientsFragment.newInstance()
        2 -> HeaderGalleryFragment.newInstance()
        else -> throw IllegalArgumentException("Invalid position: $position")
    }

    override fun getCount(): Int {
        return 3
    }
}