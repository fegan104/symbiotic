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
import kotlinx.android.synthetic.main.ingredients_fragment.*


/**
 * This holds the list of ingredients in our kombucha.
 */
class IngredientsFragment : Fragment() {
    private val factory by lazy { injector.addEditViewModelFactory() }
    private val viewModel by viewModels<AddEditViewModel>(
        ownerProducer = ::requireParentFragment,
        factoryProducer = { factory }
    )

    private lateinit var adapter: IngredientsAdapter

    private inner class IngredientsAdapter(
        context: Context,
        resource: Int = com.frankegan.symbiotic.R.layout.ingredients_item,
        val items: ArrayList<Ingredient>
    ) : ArrayAdapter<Ingredient>(context, resource, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val ingredientView = convertView ?: LayoutInflater
                .from(requireContext())
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.frankegan.symbiotic.R.layout.ingredients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.ingredientData.observe(this) {
            adapter.updateData(it)
        }
        // Inflate the layout for this fragment
        adapter = IngredientsAdapter(
            context = requireContext(),
            items = arrayListOf()
        )
        list_view.adapter = adapter

        add_button.setOnClickListener {
            viewModel.addIngredient(
                name = ingredient_name_input.text.toString(),
                quantity = ingredient_quantity_input.text.toString().toDoubleOrNull() ?: 0.0,
                unit = exposed_dropdown.editableText.toString()
            )
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance() = IngredientsFragment()
    }
}
