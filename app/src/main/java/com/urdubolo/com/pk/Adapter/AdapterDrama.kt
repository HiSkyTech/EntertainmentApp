package com.urdubolo.com.pk.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.R

class AdapterDrama(private val dramaList: List<ModelDramaItem>,var context: Context,var listener:ItemcClicklistner) :
    RecyclerView.Adapter<AdapterDrama.DramaViewHolder>() {




        interface ItemcClicklistner
        {
            fun OnitemClick(dramaId: String)
        }



    inner class DramaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dramaThumbnail: ImageView = itemView.findViewById(R.id.dramaBanner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DramaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.darama_dose_item, parent, false)
        return DramaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DramaViewHolder, position: Int) {

        val currentItem = dramaList[position]
        val fullUrl = "https://hiskytechs.com/video_adminpenal/${currentItem.thumbnail}"
        Glide.with(context).load(fullUrl).
        placeholder(R.drawable.logoimg).error(R.drawable.logoimg).into(holder.dramaThumbnail)
holder.dramaThumbnail.setOnClickListener()
{
    listener.OnitemClick(currentItem.id)
}

    }

    override fun getItemCount(): Int {
        return dramaList.size
    }
}
