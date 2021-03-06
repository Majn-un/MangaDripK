package com.example.mangadripk.Activity

//import com.example.mangadripk.Fragments.FavoriteFragment
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mangadripk.Adapter.PageAdapter
import com.example.mangadripk.Fragments.Download
import com.example.mangadripk.Fragments.Favorite
import com.example.mangadripk.Fragments.Library
import com.example.mangadripk.Fragments.Recent
import com.example.mangadripk.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_library.*

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
                R.id.nav_download -> selectedFragment = Download()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }
}