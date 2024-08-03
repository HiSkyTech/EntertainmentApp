package com.urdubolo.com.pk.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.urdubolo.com.pk.Adapters.AdapterDrama
import com.urdubolo.com.pk.Adapters.AdapterSeasonsHome
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.*
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Ui.ActivityDramaViewMore
import com.urdubolo.com.pk.Ui.ActivityOnDramaClick
import com.urdubolo.com.pk.Ui.ActivitySeasonsViewMore
import com.urdubolo.com.pk.Util.MySharedPref
import com.urdubolo.com.pk.Util.UtilAnimation
import com.urdubolo.com.pk.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), AdapterDrama.ItemcClicklistner, AdapterSeasonsHome.ItemcClicklistner {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiInterface: ApiInterFace
    private val TAG = "HomeFragment"
    private lateinit var player: ExoPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


setupPlayer()

        binding.apply {

            btnTrending.setOnClickListener()
            {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()
            }
            btnUpcoming.setOnClickListener()
            {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()

            }

            btnRecomended.setOnClickListener()
            {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()

            }


            search.setOnClickListener()
            {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()

            }


            searchMain.setOnClickListener()
            {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()

            }


            viewMoreDrama.setOnClickListener()
            {
                startActivity(Intent(requireContext(), ActivityDramaViewMore::class.java))

            }

            viewMoreSeasons.setOnClickListener()
            {
                startActivity(Intent(requireContext(), ActivitySeasonsViewMore::class.java))

            }

        }
        var utilAnimation=UtilAnimation(requireContext())
        utilAnimation.startLoadingAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
        utilAnimation.endLoadingAnimation()
        }, 6000)

        apiInterface = RetrofitClient.apiInterface
        val fullUrl = "https://hiskytechs.com/video_adminpenal/${MySharedPref.getUserModel()?.profile_image}"
        Glide.with(requireContext()).load(fullUrl)
            .placeholder(R.drawable.logoimg)
            .error(R.drawable.logoimg)
            .into(binding.ivProfile)

        // Get dramas
        getDramas()

        // Get seasons
        getSeasons()

        return binding.root
    }


    private fun setupPlayer() {

        player = ExoPlayer.Builder(requireContext()).build()
        binding.videobanner.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse("https://hiskytechs.com/video_adminpenal/videos/urdu_bolo_intro.mp4"))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        player.volume = 0f // Mute the video

        // Customize the PlayerView
        val playerView = binding.videobanner
        playerView.useController = false // Remove all controllers
       // Make video fill the PlayerView
    }



/*    private fun setupPlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.videobanner.player = player

        val mediaItem = MediaItem.fromUri(Uri.parse("https://hiskytechs.com/video_adminpenal/videos/urdu_bolo_intro.mp4"))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true

        // Customize the PlayerView
        val playerView = binding.videobanner.findViewById<PlayerView>(R.id.videobanner)
        playerView.showController()
        playerView.controllerAutoShow = true
        playerView.setControllerVisibilityListener { visibility ->
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)
            playerView.setShowFastForwardButton(false)
            playerView.setShowRewindButton(false)
        }
    }*/


    private fun getDramas() {


        val call = apiInterface.getAllDramas()
        call.enqueue(object : Callback<ModelDrama> {
            override fun onResponse(call: Call<ModelDrama>, response: Response<ModelDrama>) {
//utilAnimation.endLoadingAnimation()
                if (response.isSuccessful) {
                    var dramaList = response.body() ?: ArrayList<ModelDramaItem>()
                    if (dramaList.size > 3) {
                        dramaList = ArrayList(dramaList.take(3))
                    }
                    binding.rvDrama.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.rvDrama.adapter = AdapterDrama(dramaList, requireContext(), this@HomeFragment)
                    Log.d(TAG, "Dramas fetched successfully.")
                } else {
                    Log.e(TAG, "Failed to fetch dramas: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelDrama>, t: Throwable) {
          //      utilAnimation.endLoadingAnimation()

                Log.e(TAG, "Error fetching dramas: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching dramas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getSeasons() {
        val callSeasons = apiInterface.getAllSeasons()
        callSeasons.enqueue(object : Callback<ModelSeason> {
            override fun onResponse(call: Call<ModelSeason>, response: Response<ModelSeason>) {
                if (response.isSuccessful) {
                    val seasonResponse = response.body()
                    if (seasonResponse != null) {
                        var seasonList = seasonResponse.seasons
                        if (seasonList.size > 3) {
                            seasonList = seasonList.take(1)
                        }
var modelSeason=seasonList[0]

                        binding.seasonNumber.text= modelSeason.season_number.toString()
                        binding.toatalEpisodes.text=modelSeason.total_episodes.toString()
                        val fullUrl = "https://hiskytechs.com/video_adminpenal/${modelSeason.thumbnail}"
                        Glide.with(requireContext()).load(fullUrl).
                        placeholder(R.drawable.logoimg).error(R.drawable.logoimg).into(binding.seasonThumnail)


                       /* binding.recycle1.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        binding.recycle1.adapter = AdapterSeasonsHome(seasonList, requireContext(), this@HomeFragment)
                        Log.d(TAG, "Seasons fetched successfully.")*/
                    } else {
                        Toast.makeText(requireContext(), "No seasons found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "Failed to fetch seasons: ${response.code()} - ${response.message()}")
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelSeason>, t: Throwable) {
                Log.e(TAG, "Error fetching seasons: ${t.message}")
                Toast.makeText(requireContext(), "Error fetching seasons", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun OnitemClick(dramaId: String) {
        startActivity(Intent(requireContext(), ActivityOnDramaClick::class.java).putExtra("dramaId", dramaId))
    }

    override fun OnSeasonitemClick(seasonId: String,seasonNo:String) {
        Toast.makeText(requireContext(), seasonId, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}
