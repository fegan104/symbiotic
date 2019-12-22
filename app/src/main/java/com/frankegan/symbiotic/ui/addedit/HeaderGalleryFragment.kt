package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.VMInjectionFactory
import com.frankegan.symbiotic.di.injector
import com.stepstone.stepper.Step
import kotlinx.android.synthetic.main.header_gallery_fragment.*
import javax.inject.Inject

class HeaderGalleryFragment : Fragment(R.layout.header_gallery_fragment), Step by DefaultStepper {

    @Inject
    lateinit var adapter: HeaderGalleryAdapter

    @Inject
    lateinit var factory: VMInjectionFactory<AddEditViewModel>
    private val viewModel by activityViewModels<AddEditViewModel>(
        factoryProducer = { factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gallery_list.adapter = adapter
        adapter.onHeaderClick = { viewModel.addHeaderImage(it) }
        viewModel.headerImages().observe(this) {
            adapter.data = it
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): HeaderGalleryFragment {
            return HeaderGalleryFragment()
        }
    }
}