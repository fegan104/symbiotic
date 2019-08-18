package com.frankegan.symbiotic.ui.addedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.frankegan.symbiotic.data.Ingredient
import com.frankegan.symbiotic.di.injector
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
        return inflater.inflate(com.frankegan.symbiotic.R.layout.ingredients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_view.adapter = IngredientsAdapter(
            context = requireContext(),
            items = arrayListOf()
        ).apply { adapter = this }
        viewModel.ingredientData.observe(this) {
            adapter.updateData(it)
        }
        // Drop down menu
        add_button.setOnClickListener {
            viewModel.addIngredient(
                name = ingredient_name_input.text.toString(),
                quantity = ingredient_quantity_input.text.toString().toDoubleOrNull() ?: 0.0,
                unit = exposed_dropdown.editableText.toString()
            )
            ingredient_name_input.setText("")
            ingredient_quantity_input.setText("")
            exposed_dropdown.setText("")
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            arrayOf("tbsp", "tsp", "gal", "cup")
        )
        exposed_dropdown.apply {
            setAdapter(adapter)
            listSelection = 0
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_INGREDIENTS, adapter.items)
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

private class IngredientsAdapter(
    context: Context,
    resource: Int = com.frankegan.symbiotic.R.layout.ingredients_item,
    val items: ArrayList<Ingredient>
) : ArrayAdapter<Ingredient>(context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val ingredientView = convertView ?: LayoutInflater
            .from(context)
            .inflate(com.frankegan.symbiotic.R.layout.ingredients_item, parent, false)
        items[position].apply {
            ingredientView.findViewById<TextView>(com.frankegan.symbiotic.R.id.name_text).text = name
            ingredientView.findViewById<TextView>(com.frankegan.symbiotic.R.id.quantityt_text).text =
                quantity.toString()
            ingredientView.findViewById<TextView>(com.frankegan.symbiotic.R.id.unit_text).text = unit
        }
        return ingredientView
    }

    fun updateData(new: List<Ingredient>) {
        clear()
        new.forEachIndexed { index, ingredient -> insert(ingredient, index) }
        notifyDataSetChanged()
    }
}
