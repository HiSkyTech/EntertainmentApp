package com.urdubolo.com.pk.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.urdubolo.com.pk.Adapters.AdapterEpisodes
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.UserIdVideoRequest
import com.urdubolo.com.pk.Model.ModelAssignedVideo
import com.urdubolo.com.pk.Model.ModelEpisode
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.Ui.ActivtyPlayer
import com.urdubolo.com.pk.databinding.FragmentPrivateChannelBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrivateChannelFragment : Fragment(), AdapterEpisodes.EpisodeItemClicklistner {
    private var _binding: FragmentPrivateChannelBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiInterface: ApiInterFace

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrivateChannelBinding.inflate(inflater, container, false)

        apiInterface = RetrofitClient.apiInterface

        val call = apiInterface.getSpecifiedAssignedVideo(UserIdVideoRequest(1))



        call.enqueue(object : Callback<ModelAssignedVideo> {
            override fun onResponse(call: Call<ModelAssignedVideo>, response: Response<ModelAssignedVideo>) {
                if (response.isSuccessful) {
                    val episodes = response.body()?.videos


                    if (episodes != null) {
                        val privateVideoList = episodes.filter { it.privacy == "private" }

                        binding.recentlyunlockedrv.layoutManager = LinearLayoutManager(requireContext())
                             binding.recentlyunlockedrv.adapter = AdapterEpisodes("", privateVideoList, requireContext(), this@PrivateChannelFragment)
                    } else {
                        Toast.makeText(requireContext(), "No Episodes Available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelAssignedVideo>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })





        /*call.enqueue(object : Callback<ModelAssignedVideo> {
            override fun onResponse(call: Call<ModelAssignedVideo>, response: Response<ModelAssignedVideo>) {
                if (response.isSuccessful) {
                    val episodes = response.body()?.videos

                    // Log or Toast to check if data is fetched
                    Toast.makeText(requireContext(), response.body()?.success.toString(), Toast.LENGTH_SHORT).show()

                    if (episodes != null) {
                        val privateVideoList = episodes.filter { it.privacy == "private" }

                        // Check the filtered list size
                        Toast.makeText(requireContext(), "Private Videos: ${privateVideoList.size}", Toast.LENGTH_SHORT).show()

                        binding.rvEditorPicks.layoutManager = LinearLayoutManager(requireContext())
                   //     binding.rvEditorPicks.adapter = AdapterEpisodes("", privateVideoList, requireContext(), this@PrivateChannelFragment)
                    } else {
                        Toast.makeText(requireContext(), "No Episodes Available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelAssignedVideo>, t: Throwable) {
                Toast.makeText(requireContext(), "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })*/

        return binding.root
    }

    override fun OnVideoitemClick(videoUrl: String) {
        startActivity(Intent(requireContext(), ActivtyPlayer::class.java).putExtra("videourl",videoUrl))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
