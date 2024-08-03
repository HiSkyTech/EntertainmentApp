package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.urdubolo.com.pk.Model.UserData
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.ActivityLoginBinding
import com.urdubolo.com.pk.Interfaces.ApiInterFace
import com.urdubolo.com.pk.Interfaces.UserLoginRequest
import com.urdubolo.com.pk.Model.ModelUserResponse
import com.urdubolo.com.pk.Model.RetrofitClient
import com.urdubolo.com.pk.Util.MySharedPref
import com.urdubolo.com.pk.Util.UtilAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiInterFace: ApiInterFace
    private lateinit var utilAnimation: UtilAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        utilAnimation = UtilAnimation(this@ActivityLogin)
        apiInterFace = RetrofitClient.apiInterface

        binding.apply {
            btnSignIn.setOnClickListener {
                val email = etLogin.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    loginUser(email, password)
                } else {
                    Toast.makeText(this@ActivityLogin, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                }
            }


            tvSignUp.setOnClickListener()
            {
                startActivity(Intent(this@ActivityLogin,ActivitySignUp::class.java))
            }
         tvForget.setOnClickListener()
            {
                Toast.makeText(this@ActivityLogin, "AvailableSoon!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val userLoginRequest = UserLoginRequest(email, password)
        utilAnimation.startLoadingAnimation()
        apiInterFace.loginUser(userLoginRequest).enqueue(object : Callback<ModelUserResponse> {
            override fun onResponse(call: Call<ModelUserResponse>, response: Response<ModelUserResponse>) {
                utilAnimation.endLoadingAnimation()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null && userResponse.message == "Login successful") {
                        val userData = userResponse.user_data
                        if (userData.logged_number <= 1) {
                            Toast.makeText(this@ActivityLogin, "Login Successful!!", Toast.LENGTH_SHORT).show()
                            MySharedPref.saveUserModel(userData)
                            MySharedPref.saveUserLoggedIn()
                            startActivity(Intent(this@ActivityLogin, MainActivity::class.java))
                            finishAffinity()
                        } else {
                            Toast.makeText(this@ActivityLogin, "Already Logged In on another device", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ActivityLogin, "Login Failed: ${userResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ActivityLogin, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ModelUserResponse>, t: Throwable) {
                utilAnimation.endLoadingAnimation()
                Toast.makeText(this@ActivityLogin, "Something went wrong!!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
