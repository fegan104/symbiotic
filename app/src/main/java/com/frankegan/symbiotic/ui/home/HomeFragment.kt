package com.frankegan.symbiotic.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.injector
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    /**
     * We need to inject a ViewModelFactory to build our ViewModels with custom parameters.
     */
    private val factory by lazy { injector.homeViewModelFactory() }
    private val viewModel by viewModels<HomeViewModel>(
        factoryProducer = { factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addEditAction(fermentationId = null))
        }
        viewModel.fermentationData().observe(this) {
            recycler_view.adapter = FermentationAdapter {
                findNavController().navigate(HomeFragmentDirections.addEditAction(fermentationId = it.id))
            }.apply {
                updateItems(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fermentationData()
    }
}
