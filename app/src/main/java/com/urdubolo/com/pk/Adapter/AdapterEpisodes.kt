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
import com.urdubolo.com.pk.Model.ModelEpisodeItem
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.R

class AdapterEpisodes(private  var seasonNo:String,private val episodeList: List<ModelEpisodeItem>,var context: Context,var listener:EpisodeItemClicklistner) :
    RecyclerView.Adapter<AdapterEpisodes.DramaViewHolder>() {




    interface EpisodeItemClicklistner
    {
        fun OnVideoitemClick(videoUrl: String)
    }



    inner class DramaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val episodeThumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
         val seasonNo: TextView = itemView.findViewById(R.id.tvSeasonNo)
        val layEpisode: RelativeLayout = itemView.findViewById(R.id.layEpisode)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val episodeNo: TextView = itemView.findViewById(R.id.tvEpisodeNumber)
        val date: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DramaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_season_episode, parent, false)
        return DramaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DramaViewHolder, position: Int) {

        val currentItem = episodeList[position]
        if(currentItem.privacy != "private")
        {
            holder.date.text=currentItem.created_at.toString()
            holder.episodeNo.text=currentItem.episode_number.toString()
            holder.description.text=currentItem.description
            holder.seasonNo.text=seasonNo
            val fullUrl = "https://hiskytechs.com/video_adminpenal/${currentItem.thumbnail}"
            Glide.with(context).load(fullUrl).
            placeholder(R.drawable.logoimg).error(R.drawable.logoimg).into(holder.episodeThumbnail)
        }

        holder.layEpisode.setOnClickListener()
        {
            listener.OnVideoitemClick(currentItem.video_path.toString())
        }

    }

    override fun getItemCount(): Int {
        return episodeList.size
    }
}
