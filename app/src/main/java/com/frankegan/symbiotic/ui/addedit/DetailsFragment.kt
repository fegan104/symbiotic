package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
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
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.details_fragment.*


const val DETAIL_DATE_FORMAT = "EEE MMM. dd, yyyy, HH:mm"
private const val MISSING_NAME = "MISSING_NAME"

/**
 * A simple [Fragment] subclass.
 *
 * This contains the fermentations name, date and time for notifications and extra notes about the fermentation.
 */
class DetailsFragment : Fragment(), Step {
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
        ///////////////////////////
        //Observe ViewModel LiveData
        ///////////////////////////
        viewModel.fermentationData.observe(this) {
            it ?: return@observe
            if (name_input?.text?.isBlank() == true || (name_input?.text.toString() != it.title)) {
                name_input.setText(it.title)
                start_date_input.setText(it.startDate.format(DETAIL_DATE_FORMAT))
                f1_date_input.setText(it.firstEndDate.format(DETAIL_DATE_FORMAT))
                f2_date_input.setText(it.secondEndDate.format(DETAIL_DATE_FORMAT))
            } else {
                if (name_input.text.toString() == it.title) return@observe
                viewModel.addDetails(
                    name = name_input.text.toString(),
                    start = start_date_input.text.toString(),
                    first = f1_date_input.text.toString(),
                    second = f2_date_input.text.toString()
                )
            }
        }
        viewModel.noteData.observe(this) {
            it ?: return@observe
            if (notes_input.text.toString() == it.content) return@observe
            notes_input.setText(it.content)
        }

        ///////////////////////////
        //Save view state in ViewModel
        ///////////////////////////
        name_input.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.addDetails(name = name_input.text.toString())
        }
        start_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                val str = dateTime.format(DETAIL_DATE_FORMAT)
                start_date_input.setText(str)
                viewModel.addDetails(start = str)
            }
        }
        f1_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                val str = dateTime.format(DETAIL_DATE_FORMAT)
                f1_date_input.setText(str)
                viewModel.addDetails(first = str)
            }
        }
        f2_date_input.setOnClickListener {
            lifecycleScope.launchSilent {
                val dateTime = openDateTimeDialog()
                val str = dateTime.format(DETAIL_DATE_FORMAT)
                f2_date_input.setText(str)
                viewModel.addDetails(second = str)
            }
        }
        notes_input.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) viewModel.addNote(content = notes_input.text.toString())
        }
    }

    override fun onSelected() = Unit

    override fun verifyStep(): VerificationError? {
        return if (name_input.text.isNullOrBlank()) {
            VerificationError(MISSING_NAME)
        } else null
    }

    override fun onError(error: VerificationError) {
        if (error.errorMessage == MISSING_NAME) {
            name_input_layout.error = getString(R.string.please_add_name)
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



