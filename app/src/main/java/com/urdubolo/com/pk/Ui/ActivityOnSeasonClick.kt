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
import com.urdubolo.com.pk.Adapters.AdapterEpisodes
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.SeasonIdRequest
import com.urdubolo.com.pk.Model.ModelEpisodeItem
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.ActivityOnSeasonClickBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityOnSeasonClick : AppCompatActivity(),AdapterEpisodes.EpisodeItemClicklistner {
    private lateinit var binding: ActivityOnSeasonClickBinding
    private var seasonNo="0"
    private lateinit var apiInterface:ApiInterFace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOnSeasonClickBinding.inflate(layoutInflater)
        setContentView(binding.root)
var seasonId=intent.getStringExtra("seasonid")!!
 seasonNo=intent.getStringExtra("seasonNo")!!
        apiInterface=RetrofitClient.apiInterface
        getEpisodes(seasonId)
    }

    private fun getEpisodes(seasonId:String)
    {
        val seasonIdRequest = SeasonIdRequest(seasonId.toInt())

        val call = apiInterface.getSeasonsEpisodes(seasonIdRequest)

        call.enqueue(object : Callback<List<ModelEpisodeItem>> {
            override fun onResponse(call: Call<List<ModelEpisodeItem>>, response: Response<List<ModelEpisodeItem>>) {
                if (response.isSuccessful) {
                    var episodeList = ArrayList<ModelEpisodeItem>()
                    response.body()?.let {
                        episodeList = ArrayList(it)
                    }

                     binding.rvEpisodes.layoutManager = LinearLayoutManager(this@ActivityOnSeasonClick)
                    binding.rvEpisodes.adapter = AdapterEpisodes(seasonNo, episodeList, this@ActivityOnSeasonClick, this@ActivityOnSeasonClick)

                } else {
                    Toast.makeText(this@ActivityOnSeasonClick, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ModelEpisodeItem>>, t: Throwable) {
                Log.e("TAG", t.toString())
                Toast.makeText(this@ActivityOnSeasonClick, "Failure", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnVideoitemClick(videoUrl: String) {
        startActivity(Intent(this@ActivityOnSeasonClick,ActivtyPlayer::class.java).putExtra("videourl",videoUrl))

    }
}