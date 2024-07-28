package com.urdubolo.com.pk.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.urdubolo.com.pk.Adapters.AdapterDrama
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.ModelDramaItem
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Ui.ActivityOnDramaClick
import com.urdubolo.com.pk.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(),AdapterDrama.ItemcClicklistner {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    //object dekaclare
    private lateinit var apiInterface: ApiInterFace
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

///intilizie
        apiInterface = RetrofitClient.apiInterface
var dramaList=ArrayList<ModelDramaItem>()

        //jo cheez get krni hai ya post ya even kuch bhi

        val call = apiInterface.getAllDramas()

        call.enqueue(object : Callback<ModelDrama> {
            override fun onResponse(call: Call<ModelDrama>, response: Response<ModelDrama>) {
                if (response.isSuccessful) {

                    dramaList = response.body()!!
                    binding.rvDrama.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    binding.rvDrama.adapter=AdapterDrama(dramaList,requireContext(),this@HomeFragment)
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }


                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ModelDrama>, t: Throwable) {
                Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show()

            }
        })




        return binding.root
    }

    override fun OnitemClick(dramaId: String) {
       startActivity(Intent(requireContext(),ActivityOnDramaClick::class.java).putExtra("dramaId",dramaId))
    }

}