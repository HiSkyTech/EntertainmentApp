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
import com.bumptech.glide.Glide
import com.urdubolo.com.pk.Adapters.AdapterSeasonsHome
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelSeason
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Util.UtilAnimation
import com.urdubolo.com.pk.databinding.ActivitySeasonsViewMoreBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivitySeasonsViewMore : AppCompatActivity() ,AdapterSeasonsHome.ItemcClicklistner{
    private lateinit var binding: ActivitySeasonsViewMoreBinding

    private lateinit var apiInterface: ApiInterFace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySeasonsViewMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        apiInterface=RetrofitClient.apiInterface
        getSeasons()



    }



    private fun getSeasons() {
        var utilAnimation=UtilAnimation(this@ActivitySeasonsViewMore)
        utilAnimation.startLoadingAnimation()
        val callSeasons = apiInterface.getAllSeasons()
        callSeasons.enqueue(object : Callback<ModelSeason> {
            override fun onResponse(call: Call<ModelSeason>, response: Response<ModelSeason>) {
                utilAnimation.endLoadingAnimation()
                if (response.isSuccessful) {
                    val seasonResponse = response.body()
                    if (seasonResponse != null) {
                        var seasonList = seasonResponse.seasons


binding.rvSeasons.layoutManager = LinearLayoutManager(this@ActivitySeasonsViewMore)
                         binding.rvSeasons.adapter = AdapterSeasonsHome(seasonList, this@ActivitySeasonsViewMore, this@ActivitySeasonsViewMore)

                    } else {
                        Toast.makeText(this@ActivitySeasonsViewMore, "No seasons found", Toast.LENGTH_SHORT).show()
                    }
                } else {

                    Toast.makeText(this@ActivitySeasonsViewMore, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelSeason>, t: Throwable) {
                utilAnimation.endLoadingAnimation()
                Toast.makeText(this@ActivitySeasonsViewMore, "Error fetching seasons", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnSeasonitemClick(seasonId: String,seasonNo:String) {
        startActivity(Intent(this@ActivitySeasonsViewMore, ActivityOnSeasonClick::class.java).putExtra("seasonid", seasonId).putExtra("seasonNo",seasonNo))

    }
}