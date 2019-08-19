package com.frankegan.symbiotic.ui.addedit

import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.FragmentManager
import com.frankegan.symbiotic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


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
        val titles = context.resources.getStringArray(R.array.add_edit)
        return StepViewModel.Builder(context)
            .setTitle(titles[position]) //can be a CharSequence instead
            .create()
    }
}