package com.frankegan.symbiotic.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.VMInjectionFactory
import com.frankegan.symbiotic.di.injector
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var adapter: FermentationAdapter

    @Inject
    lateinit var factory: VMInjectionFactory<HomeViewModel>

    private val viewModel by viewModels<HomeViewModel>(
        factoryProducer = { factory }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.onItemClick = {
            findNavController().navigate(HomeFragmentDirections.addEditAction(fermentationId = it.id))
        }
        fab.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.addEditAction())
        }
        recycler_view.adapter = this.adapter
        viewModel.fermentationData().observe(this) {
            adapter.updateItems(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fermentationData()
    }
}
