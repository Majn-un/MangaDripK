package com.example.mangadripk.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mangadripk.Fragments.Favorite
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.example.mangadripk.Fragments.FavoriteFragment
import com.example.mangadripk.Fragments.Library
import com.example.mangadripk.Fragments.Recent
import com.example.mangadripk.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Library()).commit()
    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_library -> selectedFragment = Library()
                R.id.nav_favorite -> selectedFragment = Favorite()
                R.id.nav_recent -> selectedFragment = Recent()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }
}