package com.urdubolo.com.pk.Ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.Util.MySharedPref
import com.urdubolo.com.pk.databinding.ActivitySplashBinding

class ActivitySplash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.btnGetStarted.setOnClickListener()
{
    if (MySharedPref.IsUserLoggedIn()) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    } else {
        startActivity(Intent(this, ActivityLogin::class.java))
        finish()
    }

}

    }
}
