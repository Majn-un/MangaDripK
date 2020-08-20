package com.example.mangadripk.Activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Fragments.Download
import com.example.mangadripk.Fragments.Favorite
import com.google.android.material.bottomnavigation.BottomNavigationView

//import com.example.mangadripk.Fragments.FavoriteFragment
import com.example.mangadripk.Fragments.Library
import com.example.mangadripk.Fragments.Recent
import com.example.mangadripk.R
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Library()).commit()
        //toolbar
//        val mtoolbar = findViewById<Toolbar>(R.id.toolbar)
//        val mtabitemAll = findViewById<TabLayout>(R.id.All)
//        val mtabitemLatest=findViewById<TabItem>(R.id.Latest)
//        val mTabLayout = findViewById<TabLayout>(R.id.Tablayout)
//        setSupportActionBar(mtoolbar)

    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_library -> selectedFragment = Library()
                R.id.nav_favorite -> selectedFragment = Favorite()
                R.id.nav_recent -> selectedFragment = Recent()
                R.id.nav_download -> selectedFragment = Download()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }
}