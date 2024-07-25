package com.urdubolo.com.pk.Ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.urdubolo.com.pk.Fragments.DownloadFragment
import com.urdubolo.com.pk.Fragments.FavoriteFragment
import com.urdubolo.com.pk.Fragments.HomeFragment
import com.urdubolo.com.pk.Fragments.PrivateChannelFragment
import com.urdubolo.com.pk.Fragments.ProfileFragment
import com.urdubolo.com.pk.R
import com.urdubolo.com.pk.databinding.ActivityMainBinding
import com.urdubolo.com.pk.databinding.FragmentPrivateChannelBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomnavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())

                }
                R.id.nav_fav -> {
                    replaceFragment(FavoriteFragment())

                }
                R.id.nav_download -> {
                    replaceFragment(DownloadFragment())

                }
                R.id.nav_private -> {
                    replaceFragment(PrivateChannelFragment())

                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())

                }

            }
            true


        }


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
