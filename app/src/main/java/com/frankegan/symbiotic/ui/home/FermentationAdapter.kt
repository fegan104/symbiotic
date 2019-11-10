package com.frankegan.symbiotic.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Fermentation
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt


class FermentationAdapter @Inject constructor() :
    RecyclerView.Adapter<FermentationAdapter.FermentationHolder>() {
    /**
     * Backing data source.
     */
    private val items = mutableListOf<Fermentation>()
    var onItemClick: (Fermentation) -> Unit = {}

    inner class FermentationHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Fermentation) {
            view.findViewById<TextView>(R.id.item_title_text).text = item.title
            view.findViewById<ImageView>(R.id.item_thumbnail)
                .setOnClickListener { onItemClick(item) }
            val total = item.startDate.until(item.secondEndDate, ChronoUnit.SECONDS).toDouble()
            val current = item.startDate.until(LocalDateTime.now(), ChronoUnit.SECONDS).toDouble()
            view.findViewById<ProgressBar>(R.id.item_progress).progress =
                ((current / total) * 100).roundToInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FermentationHolder {
        return FermentationHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.home_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FermentationHolder, position: Int) =
        holder.bind(items[position])

    fun updateItems(newItems: List<Fermentation>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}