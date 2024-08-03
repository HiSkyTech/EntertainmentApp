package com.urdubolo.com.pk.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.R

class AdapterSeasonsHome(private val seasonList: List<ModelSeasonItem>,var context: Context,var listener:ItemcClicklistner) :
    RecyclerView.Adapter<AdapterSeasonsHome.DramaViewHolder>() {




    interface ItemcClicklistner
    {
        fun OnSeasonitemClick(seasonId: String,seasonNo:String)
    }



    inner class DramaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* val episodeThumbnail: ImageView = itemView.findViewById(R.id.vid)
         val dramaName: TextView = itemView.findViewById(R.id.tvDramaName)
     */
        val laySeason: LinearLayout= itemView.findViewById(R.id.laySeasons)
        val toatalEpisodes: TextView = itemView.findViewById(R.id.toatalEpisodes)
        val seasonNumber: TextView = itemView.findViewById(R.id.seasonNumber)
        val seasonThumbnail: ImageView = itemView.findViewById(R.id.seasonThumnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DramaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_seasons, parent, false)
        return DramaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DramaViewHolder, position: Int) {

        val currentItem = seasonList[position]

        holder.seasonNumber.text=currentItem.season_number.toString()

   val fullUrl = "https://hiskytechs.com/video_adminpenal/${currentItem.thumbnail}"
            Glide.with(context).load(fullUrl).
            placeholder(R.drawable.logoimg).error(R.drawable.logoimg).into(holder.seasonThumbnail)
        holder.laySeason.setOnClickListener()
        {
            listener.OnSeasonitemClick(currentItem.id.toString(),currentItem.season_number.toString())
        }
    }
    override fun getItemCount(): Int {
        return seasonList.size
    }
}
