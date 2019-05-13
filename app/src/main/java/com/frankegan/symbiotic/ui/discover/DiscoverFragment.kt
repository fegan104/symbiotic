package com.frankegan.symbiotic.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.frankegan.symbiotic.R

class DiscoverFragment : Fragment() {

    private val viewModel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_fragment, container, false)
    }

    companion object {
        fun newInstance() = DiscoverFragment()
    }

}
