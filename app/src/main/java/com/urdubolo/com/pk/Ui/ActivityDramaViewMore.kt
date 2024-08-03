package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.urdubolo.com.pk.Adapters.AdapterDrama
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Util.UtilAnimation
import com.urdubolo.com.pk.databinding.ActivityDramaViewMoreBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDramaViewMore : AppCompatActivity(),AdapterDrama.ItemcClicklistner {
    private lateinit var apiInterFace: ApiInterFace

    private lateinit var binding: ActivityDramaViewMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDramaViewMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterFace=RetrofitClient.apiInterface

        binding.ivbackArrow.setOnClickListener()
        {
            finish()
        }
        getDramas()
    }



    private fun getDramas() {
        var utilAnimation= UtilAnimation(this@ActivityDramaViewMore)
        utilAnimation.startLoadingAnimation()
        val call = apiInterFace.getAllDramas()
        call.enqueue(object : Callback<ModelDrama> {
            override fun onResponse(call: Call<ModelDrama>, response: Response<ModelDrama>) {
                utilAnimation.endLoadingAnimation()
                if (response.isSuccessful) {
                    var dramaList = response.body() ?: ArrayList<ModelDramaItem>()

                    binding.rvDramas.layoutManager = GridLayoutManager(this@ActivityDramaViewMore,2)
                    binding.rvDramas.adapter = AdapterDrama(dramaList, this@ActivityDramaViewMore, this@ActivityDramaViewMore)

                } else {

                    Toast.makeText(this@ActivityDramaViewMore, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelDrama>, t: Throwable) {
                utilAnimation.endLoadingAnimation()

                Toast.makeText(this@ActivityDramaViewMore, "Error fetching dramas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnitemClick(dramaId: String) {
        startActivity(Intent(this@ActivityDramaViewMore, ActivityOnDramaClick::class.java).putExtra("dramaId", dramaId))

    }

}