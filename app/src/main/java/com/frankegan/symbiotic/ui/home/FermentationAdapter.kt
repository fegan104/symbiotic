package com.frankegan.symbiotic.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.addChipsFromText
import com.frankegan.symbiotic.data.Fermentation
import com.frankegan.symbiotic.format
import com.google.android.material.chip.ChipGroup
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt


class FermentationAdapter @Inject constructor() :
    RecyclerView.Adapter<FermentationAdapter.ViewHolder>() {
    /**
     * Backing data source.
     */
    private val items = mutableListOf<Fermentation>()
    var onItemClick: (Fermentation) -> Unit = {}

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.item_title_text)
        private val subtitle: TextView = view.findViewById(R.id.item_subtitle)
        private val image: ImageView = view.findViewById(R.id.item_hero_image)
        private val chipGroup: ChipGroup = view.findViewById(R.id.item_chip_group)
        private val itemProgress: ProgressBar = view.findViewById(R.id.item_progress)

        fun bind(item: Fermentation) {
            val context = view.context
            title.text = item.title
            subtitle.text = context.getString(R.string.done_on, item.secondEndDate.format("MMM dd"))
            image.setOnClickListener { onItemClick(item) }

            val total = item.startDate.until(item.secondEndDate, ChronoUnit.SECONDS).toDouble()
            val current = item.startDate.until(LocalDateTime.now(), ChronoUnit.SECONDS).toDouble()
            itemProgress.progress = ((current / total) * 100).roundToInt()

            chipGroup.addChipsFromText(
                R.layout.home_ingredient_chip, listOf(
                    "Lemon",
                    "Black Tea",
                    "Sugar",
                    "Raspberry"
                )
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.home_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items[position])
    }

    fun updateItems(newItems: List<Fermentation>) {
        items.apply {
            clear()
            addAll(newItems)
        }
        notifyDataSetChanged()
    }
}