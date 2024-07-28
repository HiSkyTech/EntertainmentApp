package com.urdubolo.com.pk.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.urdubolo.com.pk.Adapters.AdapterDrama
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelEpisode
import com.urdubolo.com.pk.Model.ModelSeason
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.FragmentHomeBinding
import com.urdubolo.com.pk.databinding.FragmentPrivateChannelBinding
import dalvik.system.ZipPathValidator.Callback
import retrofit2.Call
import retrofit2.Response

class PrivateChannelFragment : Fragment() {
    private var _binding: FragmentPrivateChannelBinding? = null
    private val binding get() = _binding!!
    //object dekaclare
    private lateinit var apiInterface: ApiInterFace
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrivateChannelBinding.inflate(inflater, container, false)

        apiInterface = RetrofitClient.apiInterface


        val call = apiInterface.getAllEpisodes()

        call.enqueue(object : retrofit2.Callback<ModelEpisode> {
            override fun onResponse(call: Call<ModelEpisode>, response: Response<ModelEpisode>) {
                if (response.isSuccessful) {

                    val dramaList = response.body()!!

                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }


                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ModelEpisode>, t: Throwable) {
                Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show()

            }
        })



        return binding.root
    }


}