package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.urdubolo.com.pk.Adapters.AdapterDramaVideoSeasons
import com.urdubolo.com.pk.Adapters.AdapterEpisodes
import com.urdubolo.com.pk.Adapters.AdapterSeasonsHome
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.DramaIdRequest
import com.urdubolo.com.pk.Interfaces.SeasonIdRequest
import com.urdubolo.com.pk.Model.ModelEpisodeItem
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.Util.UtilAnimation
import com.urdubolo.com.pk.databinding.ActivityOnDramaClickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityOnDramaClick : AppCompatActivity(), AdapterDramaVideoSeasons.ItemcClicklistner, AdapterEpisodes.EpisodeItemClicklistner {

    private lateinit var apiInterface: ApiInterFace
    private lateinit var binding: ActivityOnDramaClickBinding
private lateinit var utilAnimation: UtilAnimation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnDramaClickBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = RetrofitClient.apiInterface
utilAnimation=UtilAnimation(this@ActivityOnDramaClick)
        val dramaId = intent.getStringExtra("dramaId")?.toIntOrNull() ?: return

        val dramaIdRequest = DramaIdRequest(dramaId)
        val call = apiInterface.getDramaSeasons(dramaIdRequest)
utilAnimation.startLoadingAnimation()
        call.enqueue(object : Callback<List<ModelSeasonItem>> {
            override fun onResponse(call: Call<List<ModelSeasonItem>>, response: Response<List<ModelSeasonItem>>) {
                utilAnimation.endLoadingAnimation()
                if (response.isSuccessful) {
                    val seasonList = ArrayList(response.body() ?: emptyList())

                    if (seasonList.isNotEmpty()) {
                        binding.rvSeasons.layoutManager = LinearLayoutManager(this@ActivityOnDramaClick, LinearLayoutManager.HORIZONTAL, false)
                        binding.rvSeasons.adapter = AdapterDramaVideoSeasons(seasonList, this@ActivityOnDramaClick, this@ActivityOnDramaClick)

                        // Load episodes for the first season by default
                        loadEpisodesForSeason(seasonList[0].id.toString(), seasonList[0].season_number.toString())
                    } else {
                        Toast.makeText(this@ActivityOnDramaClick, "No seasons found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ActivityOnDramaClick, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelSeasonItem>>, t: Throwable) {
                utilAnimation.endLoadingAnimation()
                binding.rvSeasons.adapter = AdapterDramaVideoSeasons(emptyList(), this@ActivityOnDramaClick, this@ActivityOnDramaClick)

                Log.e("TAG", t.toString())
                Toast.makeText(this@ActivityOnDramaClick, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadEpisodesForSeason(seasonId: String, seasonNo: String) {
        val seasonIdRequest = SeasonIdRequest(seasonId.toInt())
utilAnimation.startLoadingAnimation()
        val call = apiInterface.getSeasonsEpisodes(seasonIdRequest)

        call.enqueue(object : Callback<List<ModelEpisodeItem>> {
            override fun onResponse(call: Call<List<ModelEpisodeItem>>, response: Response<List<ModelEpisodeItem>>) {
                utilAnimation.endLoadingAnimation()

                if (response.isSuccessful) {
                    val episodeList = ArrayList(response.body() ?: emptyList())

                    binding.rvEpisodes.layoutManager = LinearLayoutManager(this@ActivityOnDramaClick)
                    binding.rvEpisodes.adapter = AdapterEpisodes(seasonNo, episodeList, this@ActivityOnDramaClick, this@ActivityOnDramaClick)
                    } else {
                    Toast.makeText(this@ActivityOnDramaClick, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelEpisodeItem>>, t: Throwable) {
                utilAnimation.endLoadingAnimation()
                binding.rvEpisodes.adapter = AdapterEpisodes(seasonNo, emptyList(), this@ActivityOnDramaClick, this@ActivityOnDramaClick)

                Log.e("TAG", t.toString())
                Toast.makeText(this@ActivityOnDramaClick, "No Data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnVideoitemClick(videoUrl: String) {
        startActivity(Intent(this@ActivityOnDramaClick, ActivtyPlayer::class.java).putExtra("videourl", videoUrl))
    }

    override fun OnSeasonitemClick(seasonId: String, seasonNo: String) {
        loadEpisodesForSeason(seasonId, seasonNo)
    }
}
