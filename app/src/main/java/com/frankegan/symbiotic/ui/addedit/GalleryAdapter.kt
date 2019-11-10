package com.frankegan.symbiotic.ui.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Image
import javax.inject.Inject

class GalleryAdapter @Inject constructor() : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    var onItemClick: (Image) -> Unit = {}
    val items = mutableListOf<Image>()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Image) {
            view.findViewById<TextView>(R.id.caption_text).text = item.caption
            view.findViewById<ImageView>(R.id.image_view).apply {
                load(item.fileUri)
                setOnClickListener { onItemClick(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newItems: List<Image>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}