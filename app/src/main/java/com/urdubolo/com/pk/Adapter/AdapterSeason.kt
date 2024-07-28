package com.urdubolo.com.pk.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.R

class AdapterSeason(private val seasonList: List<ModelSeasonItem>,var context: Context,var listener:ItemcClicklistner) :
    RecyclerView.Adapter<AdapterSeason.DramaViewHolder>() {




    interface ItemcClicklistner
    {
        fun OnitemClick(seasonId: String)
    }



    inner class DramaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       /* val episodeThumbnail: ImageView = itemView.findViewById(R.id.vid)
        val dramaName: TextView = itemView.findViewById(R.id.tvDramaName)
    */
        val laySeason: CardView = itemView.findViewById(R.id.laySeason)
        val seasonNumber: TextView = itemView.findViewById(R.id.seasonNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DramaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seasons, parent, false)
        return DramaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DramaViewHolder, position: Int) {

        val currentItem = seasonList[position]

        holder.seasonNumber.text=currentItem.season_number.toString()


    /*    val fullUrl = "https://hiskytechs.com/video_adminpenal/${currentItem.thumbnail}"
        Glide.with(context).load(fullUrl).
        placeholder(R.drawable.logoimg).error(R.drawable.logoimg).into(holder.episodeThumbnail)*/
        holder.laySeason.setOnClickListener()
        {
            listener.OnitemClick(currentItem.id.toString())
        }

    }

    override fun getItemCount(): Int {
        return seasonList.size
    }
}
