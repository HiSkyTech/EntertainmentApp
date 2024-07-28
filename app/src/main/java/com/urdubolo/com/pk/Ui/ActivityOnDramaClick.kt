package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.urdubolo.com.pk.Adapters.AdapterDrama
import com.urdubolo.com.pk.Adapters.AdapterEpisodes
import com.urdubolo.com.pk.Adapters.AdapterSeason
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.DramaIdRequest
import com.urdubolo.com.pk.Interfaces.SeasonIdRequest
import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.Model.ModelEpisodeItem
import com.urdubolo.com.pk.Model.ModelSeason
import com.urdubolo.com.pk.Model.ModelSeasonItem
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.ActivityOnDramaClickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ActivityOnDramaClick : AppCompatActivity(), AdapterSeason.ItemcClicklistner,AdapterEpisodes.EpisodeItemClicklistner {

    private lateinit var apiInterface: ApiInterFace
    private lateinit var binding: ActivityOnDramaClickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnDramaClickBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = RetrofitClient.apiInterface
        var seasonList = ArrayList<ModelSeasonItem>()

        val dramaId = intent.getStringExtra("dramaId")?.toIntOrNull() ?: return

        Toast.makeText(this@ActivityOnDramaClick, dramaId.toString(), Toast.LENGTH_SHORT).show()
        val dramaIdRequest = DramaIdRequest(dramaId)
        val call = apiInterface.getDramaSeasons(dramaIdRequest)

        call.enqueue(object : Callback<List<ModelSeasonItem>> {
            override fun onResponse(call: Call<List<ModelSeasonItem>>, response: Response<List<ModelSeasonItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        seasonList = ArrayList(it)
                    }

                    Toast.makeText(this@ActivityOnDramaClick, seasonList.size.toString(), Toast.LENGTH_SHORT).show()
                    binding.rvSeasons.layoutManager = LinearLayoutManager(this@ActivityOnDramaClick, LinearLayoutManager.HORIZONTAL, false)
                    binding.rvSeasons.adapter = AdapterSeason(seasonList, this@ActivityOnDramaClick, this@ActivityOnDramaClick)
                    Toast.makeText(this@ActivityOnDramaClick, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ActivityOnDramaClick, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelSeasonItem>>, t: Throwable) {
                Log.e("TAG", t.toString())
                Toast.makeText(this@ActivityOnDramaClick, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnitemClick(seasonId: String) {
        val seasonIdRequest = SeasonIdRequest(seasonId.toInt())

        val call = apiInterface.getSeasonsEpisodes(seasonIdRequest)

        call.enqueue(object : Callback<List<ModelEpisodeItem>> {
            override fun onResponse(call: Call<List<ModelEpisodeItem>>, response: Response<List<ModelEpisodeItem>>) {
                if (response.isSuccessful) {
                    var episodeList = ArrayList<ModelEpisodeItem>()
                    response.body()?.let {
                        episodeList = ArrayList(it)
                    }

                    Toast.makeText(this@ActivityOnDramaClick, episodeList.size.toString(), Toast.LENGTH_SHORT).show()
                    binding.rvEpisodes.layoutManager = LinearLayoutManager(this@ActivityOnDramaClick)
                    binding.rvEpisodes.adapter = AdapterEpisodes(episodeList, this@ActivityOnDramaClick, this@ActivityOnDramaClick)
                    Toast.makeText(this@ActivityOnDramaClick, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ActivityOnDramaClick, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelEpisodeItem>>, t: Throwable) {
                Log.e("TAG", t.toString())
                Toast.makeText(this@ActivityOnDramaClick, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnVideoitemClick(videoUrl: String) {
       startActivity(Intent(this@ActivityOnDramaClick,ActivtyPlayer::class.java).putExtra("videourl",videoUrl))
    }
}
