package com.frankegan.symbiotic.ui.addedit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Ingredient
import kotlinx.android.synthetic.main.ingredients_fragment.*

/**
 * This holds the list of ingredients in our kombucha.
 */
class IngredientsFragment : Fragment() {

    private lateinit var adapter: IngredientsAdapter

    private inner class IngredientsAdapter(
        context: Context,
        resource: Int,
        val items: Array<Ingredient>
    ) : ArrayAdapter<Ingredient>(context, resource, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val ingredientView = convertView ?: LayoutInflater
                .from(requireContext())
                .inflate(R.layout.ingredients_item, parent, false)
            items[position].apply {
                ingredientView.findViewById<TextView>(R.id.name_text).text = name
                ingredientView.findViewById<TextView>(R.id.quantityt_text).text = quantity.toString()
                ingredientView.findViewById<TextView>(R.id.unit_text).text = unit
            }
            return ingredientView
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter = IngredientsAdapter(
            requireContext(), R.layout.ingredients_item, arrayOf(
                Ingredient(
                    name = "Basil",
                    quantity = 6.0,
                    unit = "tbsp",
                    fermentation = ""
                )
            )
        )
        return inflater.inflate(R.layout.ingredients_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_view.adapter = adapter
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
