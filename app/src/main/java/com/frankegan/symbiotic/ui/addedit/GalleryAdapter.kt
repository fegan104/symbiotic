package com.frankegan.symbiotic.ui.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frankegan.symbiotic.R
import com.frankegan.symbiotic.data.Image
import java.io.File
import javax.inject.Inject

class GalleryAdapter @Inject constructor(val listener: (Image) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    val items = mutableListOf<Image>()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Image) {
            view.findViewById<TextView>(R.id.caption_text).text = item.caption
            view.findViewById<ImageView>(R.id.image_view).apply {
                Glide.with(view).load(File(item.filename)).into(this)
                setOnClickListener { listener(item) }
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