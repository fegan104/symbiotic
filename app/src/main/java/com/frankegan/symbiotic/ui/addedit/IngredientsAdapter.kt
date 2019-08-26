package com.frankegan.symbiotic.ui.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Ingredient

private const val CREATE_ITEM = 0
private const val DEFAULT_ITEM = 1

class IngredientsAdapter(
    private val createNewItem: () -> Unit,
    private val deleteItem: (Ingredient) -> Unit
) : RecyclerView.Adapter<IngredientViewHolder>() {
    //Always pad this list with a null to represent the add list item view
    private val items = mutableListOf<Ingredient?>(null)

    override fun getItemViewType(position: Int) = when {
        items.isEmpty() -> CREATE_ITEM
        position == items.count() - 1 -> CREATE_ITEM
        else -> DEFAULT_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        CREATE_ITEM -> CreateIngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ingredients_new_item,
                parent,
                false
            )
        )
        else -> DefaultIngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ingredients_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) = when (holder) {
        is CreateIngredientViewHolder -> holder.bind(createNewItem)
        is DefaultIngredientViewHolder -> holder.bind(items[position]!!, deleteItem)
    }

    fun updateData(new: List<Ingredient>) {
        items.apply {
            clear()
            addAll(new)
            add(null)
        }
        notifyDataSetChanged()
    }

    fun saveState() = ArrayList(items)
}

sealed class IngredientViewHolder(open val view: View) : RecyclerView.ViewHolder(view)

class DefaultIngredientViewHolder(override val view: View) : IngredientViewHolder(view) {
    fun bind(item: Ingredient, onClick: (Ingredient) -> Unit) {
        item.apply {
            view.findViewById<ImageView>(R.id.remove_button).setOnClickListener { onClick(item) }
            view.findViewById<TextView>(R.id.name_text).text = name
            view.findViewById<TextView>(R.id.quantityt_text).text = quantity.toString()
            view.findViewById<TextView>(R.id.unit_text).text = unit
        }
    }
}

class CreateIngredientViewHolder(override val view: View) : IngredientViewHolder(view) {
    fun bind(onClick: () -> Unit) {
        view.findViewById<View>(R.id.new_item_layout).setOnClickListener { onClick() }
    }
}
