package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frankegan.symbiotic.R

/**
 * A simple [Fragment] subclass.
 *
 * This contains the fermentations name, date and time for notifications and extra notes about the fermentation.
 */
class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }
}
