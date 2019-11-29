package com.frankegan.symbiotic.ui.addedit


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.di.VMInjectionFactory
import com.frankegan.symbiotic.di.injector
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.add_edit_fragment.*
import javax.inject.Inject


class AddEditFragment : Fragment(), StepperLayout.StepperListener {

    private val safeArgs by navArgs<AddEditFragmentArgs>()

    @Inject
    lateinit var factory: VMInjectionFactory<AddEditViewModel>

    private val viewModel by activityViewModels<AddEditViewModel>(
        factoryProducer = { factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        Log.d("AddEditFragment", safeArgs.fermentationId.toString())
        viewModel.loadFermentationData(safeArgs.fermentationId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.add_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /////////////////
        //Setup Tabs and ViewPager
        /////////////////
        stepper_layout.apply {
            adapter = AddEditStepperAdapter(childFragmentManager, requireContext())
            setCompleteButtonEnabled(true)
            setListener(this@AddEditFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_data -> {
                viewModel.deleteFermentation()
                findNavController().navigate(AddEditFragmentDirections.homeAction())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStepSelected(newStepPosition: Int) {
        val titles = requireContext().resources.getStringArray(R.array.add_edit)
        activity?.title = titles[newStepPosition]
    }

    override fun onError(verificationError: VerificationError?) = Unit

    override fun onReturn() = Unit

    override fun onCompleted(completeButton: View?) {
        viewModel.saveFermentation()
        findNavController().navigate(AddEditFragmentDirections.homeAction())
    }
}
