package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.format
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.openDateTimeDialog
import kotlinx.android.synthetic.main.details_fragment.*


const val FORMAT = "EEE MMM. dd, yyyy, HH:mm"

/**
 * A simple [Fragment] subclass.
 *
 * This contains the fermentations name, date and time for notifications and extra notes about the fermentation.
 */
class DetailsFragment : Fragment() {
    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireParentFragment,
        factoryProducer = { factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fermentationLiveData.observe(this) {
            Log.d("DetailsFragment", "observing change $it")
            name_input.setText(it.title)
            start_date_input.setText(it.startDate.format(FORMAT))
            f1_date_input.setText(it.firstEndDate.format(FORMAT))
            f2_date_input.setText(it.secondEndDate.format(FORMAT))
        }
        start_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                start_date_input.setText(dateTime.format(FORMAT))
            }
        }
        f1_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                f1_date_input.setText(dateTime.format(FORMAT))
            }
        }
        f2_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                f2_date_input.setText(dateTime.format(FORMAT))
            }
        }
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



