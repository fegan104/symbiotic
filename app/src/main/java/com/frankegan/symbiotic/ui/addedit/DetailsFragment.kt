package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.format
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.openDateTimeDialog
import kotlinx.android.synthetic.main.details_fragment.*


const val FORMAT = "EEE MMM. dd, yyyy, HH:mm"

const val KEY_NAME = "NAME"
const val KEY_START = "START"
const val KEY_FIRST = "FIRST"
const val KEY_SECOND = "SECOND"

/**
 * A simple [Fragment] subclass.
 *
 * This contains the fermentations name, date and time for notifications and extra notes about the fermentation.
 */
class DetailsFragment : Fragment() {
    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireActivity,
        factoryProducer = { factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.addDetails(
//            name = savedInstanceState?.getString(KEY_NAME) ?: "",
//            start = savedInstanceState?.getString(KEY_START) ?: "",
//            first = savedInstanceState?.getString(KEY_FIRST) ?: "",
//            second = savedInstanceState?.getString(KEY_SECOND) ?: ""
//        )

//        viewModel.fermentationData.observe(this) {
//            Log.d("Details", "Fermentation Data: $it")
//            name_input.setText(it.title)
//            start_date_input.setText(it.startDate.format(FORMAT))
//            f1_date_input.setText(it.firstEndDate.format(FORMAT))
//            f2_date_input.setText(it.secondEndDate.format(FORMAT))
//        }

//        name_input.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) viewModel.addDetails(name = name_input.text.toString())
//        }
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

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(KEY_NAME, (name_input?.text ?: "").toString())
//        outState.putString(KEY_START, (start_date_input?.text ?: "").toString())
//        outState.putString(KEY_FIRST, (f1_date_input?.text ?: "").toString())
//        outState.putString(KEY_SECOND, (f2_date_input?.text ?: "").toString())
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }
}



