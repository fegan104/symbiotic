package com.frankegan.symbiotic.ui.addedit

import android.util.Log
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

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private val items = mutableListOf<Image>()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Image, listener: (Image) -> Unit) {
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
        holder.bind(items[position]) {
            Log.d("Gallery", it.toString())
        }
    }

    fun updateItems(newItems: List<Image>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}