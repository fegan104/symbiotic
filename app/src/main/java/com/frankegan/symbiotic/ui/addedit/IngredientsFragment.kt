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
import com.frankegan.symbiotic.data.Ingredient
import com.frankegan.symbiotic.di.injector
import com.frankegan.symbiotic.ingredientInputDialog
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.ingredients_fragment.*

const val KEY_INGREDIENTS = "INGREDIENTS"

/**
 * This holds the list of ingredients in our kombucha.
 */
class IngredientsFragment : Fragment(), Step {
    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireActivity,
        factoryProducer = { factory }
    )

    private lateinit var adapter: IngredientsAdapter

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
        list_view.adapter = IngredientsAdapter(
            createNewItem = { showInputDialog() },
            deleteItem = { viewModel.deleteIngredient(it) }
        ).also { adapter = it }
        viewModel.ingredientData.observe(this) {
            adapter.updateData(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_INGREDIENTS, adapter.saveState())
    }

    private fun showInputDialog() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val ingredients = ingredientInputDialog()
        viewModel.addIngredient(ingredients.first, ingredients.second, ingredients.third)
    }

    override fun onSelected() = Unit

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) = Unit

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = IngredientsFragment()
    }
}
