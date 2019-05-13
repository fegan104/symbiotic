package com.frankegan.symbiotic.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.frankegan.symbiotic.R

class ChartFragment : Fragment() {

    companion object {
        fun newInstance() = ChartFragment()
    }

    private lateinit var viewModel: ChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChartViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
