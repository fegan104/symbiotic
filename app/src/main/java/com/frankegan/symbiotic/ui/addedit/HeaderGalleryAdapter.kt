package com.frankegan.symbiotic.ui.addedit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.frankegan.symbiotic.R
import javax.inject.Inject

class HeaderGalleryAdapter @Inject constructor() :
    RecyclerView.Adapter<HeaderGalleryAdapter.ViewHolder>() {
    var onHeaderClick: (imageUrl: String) -> Unit = {}

    var data: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val headerImageView = itemView.findViewById<ImageView>(R.id.header_image_view)

        fun bind(headerUrl: String, onHeaderClick: (imageUrl: String) -> Unit) {
            headerImageView.load(headerUrl)
            headerImageView.setOnClickListener {
                onHeaderClick(headerUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.header_gallery_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], onHeaderClick)
    }
}