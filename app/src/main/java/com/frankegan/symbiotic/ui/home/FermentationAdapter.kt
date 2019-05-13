package com.frankegan.symbiotic.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Fermentation

class FermentationAdapter : RecyclerView.Adapter<FermentationAdapter.FermentationHolder>() {
    /**
     * Backing data source.
     */
    private val items = mutableListOf<Fermentation>()

    inner class FermentationHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Fermentation, listener: (Fermentation) -> Unit) {
            view.findViewById<TextView>(R.id.item_title_text).text = item.title
            view.findViewById<ImageView>(R.id.item_thumbnail).setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FermentationHolder {
        return FermentationHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_list_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FermentationHolder, position: Int) {
        holder.bind(items[position]) {
            Log.d("Home", it.toString())
        }
    }

    fun updateItems(newItems: List<Fermentation>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}