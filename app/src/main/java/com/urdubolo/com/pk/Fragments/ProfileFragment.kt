package com.urdubolo.com.pk.Fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.urdubolo.com.pk.ActivityDeleteAccount
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.UserLogoutId
import com.urdubolo.com.pk.Model.ModelLogoutResponse
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Ui.ActivityLogin
import com.urdubolo.com.pk.Util.MySharedPref
import com.urdubolo.com.pk.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiInterface: ApiInterFace

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setData()
        apiInterface = RetrofitClient.apiInterface

        binding.apply {
            cvAbout.setOnClickListener {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()
            }
            cvTerms.setOnClickListener {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()
            }
            cvLogout.setOnClickListener {
                showLogoutConfirmationDialog()
            }
            search.setOnClickListener {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()
            }
            imgbell.setOnClickListener {
                Toast.makeText(requireContext(), "Available Soon!!", Toast.LENGTH_SHORT).show()
            }
            del.setOnClickListener {
              startActivity(Intent(requireContext(),ActivityDeleteAccount::class.java))
            }
        }
        return binding.root
    }

    private fun setData() {
        val userData = MySharedPref.getUserModel()!!

        binding.apply {
            tvUserEmail.text = userData.email
            tvName1.text = userData.username
            tvUserName.text = userData.username
            val fullUrl = "https://hiskytechs.com/video_adminpenal/${userData.profile_image}"
            Glide.with(requireContext()).load(fullUrl)
                .placeholder(R.drawable.logoimg)
                .error(R.drawable.logoimg)
                .into(imgProfile)
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                handleLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun handleLogout() {
        val userId = MySharedPref.getUserModel()?.id
        if (userId != null) {
            apiInterface.logoutUser(UserLogoutId(userId)).enqueue(object : Callback<ModelLogoutResponse> {
                override fun onResponse(call: Call<ModelLogoutResponse>, response: Response<ModelLogoutResponse>) {
                    if (response.isSuccessful) {
                        val logoutResponse = response.body()
                        if (logoutResponse != null) {
                            Toast.makeText(requireContext(), logoutResponse.message, Toast.LENGTH_SHORT).show()
                            MySharedPref.clearPreferences()

                            val intent = Intent(requireContext(), ActivityLogin::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), "Logout failed. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d(TAG, "Failed to logout: ${response.code()} - ${response.message()}")
                        Toast.makeText(requireContext(), "Failed to logout", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ModelLogoutResponse>, t: Throwable) {
                    Log.e(TAG, "Error during logout: ${t.message}")
                    Toast.makeText(requireContext(), "Error during logout", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "User ID not found", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "ProfileFragment"
    }
}
