package com.urdubolo.com.pk.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Model.ModelDrama
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var apiInterface: ApiInterFace
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        apiInterface = RetrofitClient.apiInterface
        val call = apiInterface.getAllDramas()
        call.enqueue(object : Callback<ModelDrama> {
            override fun onResponse(call: Call<ModelDrama>, response: Response<ModelDrama>) {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ModelDrama>, t: Throwable) {
                Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show()

            }
        })


        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}