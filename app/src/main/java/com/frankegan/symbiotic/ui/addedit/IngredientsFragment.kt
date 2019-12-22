package com.frankegan.symbiotic.ui.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Ingredient
import com.frankegan.symbiotic.di.VMInjectionFactory
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.ingredientInputDialog
import com.stepstone.stepper.Step
import kotlinx.android.synthetic.main.ingredients_fragment.*
import javax.inject.Inject

const val KEY_INGREDIENTS = "INGREDIENTS"

/**
 * This holds the list of ingredients in our kombucha.
 */
class IngredientsFragment : Fragment(), Step by DefaultStepper {
    @Inject
    lateinit var adapter: IngredientsAdapter

    @Inject
    lateinit var factory: VMInjectionFactory<AddEditViewModel>

    private val viewModel by activityViewModels<AddEditViewModel>(
        factoryProducer = { factory }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        savedInstanceState?.getParcelableArrayList<Ingredient>(KEY_INGREDIENTS)?.forEach {
            viewModel.addIngredient(it.name, it.quantity, it.unit)
        }
        return inflater.inflate(R.layout.ingredients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_ingredient.setOnClickListener { showInputDialog() }

        list_view.adapter = this.adapter.apply {
            deleteItem = { viewModel.deleteIngredient(it) }
        }

        viewModel.ingredientData.observe(this) {
            this.adapter.updateData(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_INGREDIENTS, adapter.saveState())
    }

    private fun showInputDialog() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val ingredients = ingredientInputDialog()
        viewModel.addIngredient(
            name = ingredients.first,
            quantity = ingredients.second,
            unit = ingredients.third
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = IngredientsFragment()
    }
}
