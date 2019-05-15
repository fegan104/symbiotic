package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.format
import com.frankegan.symbiotic.launchSilent
import com.frankegan.symbiotic.openDateTimeDialog
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


const val FORMAT = "EEE MMM. dd, yyyy, HH:mm"

/**
 * A simple [Fragment] subclass.
 *
 * This contains the fermentations name, date and time for notifications and extra notes about the fermentation.
 */
class DetailsFragment : Fragment() {
    private lateinit var job: Job
    private val uiScope: CoroutineScope
        get() = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.details_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_date_input.setOnClickListener {
            uiScope.launchSilent {
                val dateTime = openDateTimeDialog()
                start_date_input.setText(dateTime.format(FORMAT))
            }
        }
        f1_date_input.setOnClickListener {
            uiScope.launchSilent {
                val dateTime = openDateTimeDialog()
                f1_date_input.setText(dateTime.format(FORMAT))
            }
        }
        f2_date_input.setOnClickListener {
            uiScope.launchSilent {
                val dateTime = openDateTimeDialog()
                f2_date_input.setText(dateTime.format(FORMAT))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
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



