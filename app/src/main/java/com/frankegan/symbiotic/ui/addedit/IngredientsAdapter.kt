package com.frankegan.symbiotic.ui.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Ingredient
import javax.inject.Inject


class IngredientsAdapter @Inject constructor() :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {
    //Always pad this list with a null to represent the add list item view
    private val items = mutableListOf<Ingredient>()
    var deleteItem: (Ingredient) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.ingredients_item,
            parent,
            false
        )
        return ViewHolder(layout)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], deleteItem)
    }

    fun updateData(new: List<Ingredient>) {
        items.apply {
            clear()
            addAll(new)
            reverse()
        }
        notifyDataSetChanged()
    }

    fun saveState() = ArrayList(items)

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Ingredient, onClick: (Ingredient) -> Unit) {
            item.apply {
                view.findViewById<ImageView>(R.id.remove_button)
                    .setOnClickListener { onClick(item) }
                view.findViewById<TextView>(R.id.name_text).text = name
                view.findViewById<TextView>(R.id.quantityt_text).text = quantity.toString()
                view.findViewById<TextView>(R.id.unit_text).text = unit
            }
        }
    }
}

