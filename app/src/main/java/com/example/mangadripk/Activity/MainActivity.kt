package com.example.mangadripk.Activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.mangadripk.Fragments.Download
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
                R.id.nav_download -> selectedFragment = Download()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

//        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

//        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
    }
}