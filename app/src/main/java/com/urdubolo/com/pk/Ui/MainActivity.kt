package com.urdubolo.com.pk.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPagerAdapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        val tabLayoutMediator = TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Favorite"
                2 -> tab.text = "Download"
                3 -> tab.text = "Private"
                4 -> tab.text = "Profile"
            }
        }
        tabLayoutMediator.attach()
    }
}
