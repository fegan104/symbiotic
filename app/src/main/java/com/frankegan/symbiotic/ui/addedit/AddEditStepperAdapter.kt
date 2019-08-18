package com.frankegan.symbiotic.ui.addedit

import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

private const val CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION"

class AddEditStepperAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step = when (position) {
        0 -> DetailsFragment.newInstance()
        1 -> IngredientsFragment.newInstance()
        2 -> GalleryFragment.newInstance()
        else -> throw Error("Impossible state in ViewPager")
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return StepViewModel.Builder(context)
            .setTitle("AddEdit") //can be a CharSequence instead
            .create()
    }
}