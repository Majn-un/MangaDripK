package com.example.mangadripk.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.R
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNewManga()

        val myrv = findViewById<View>(R.id.recyclerview_id) as RecyclerView
        myAdapter = RecyclerViewAdapter(this, mangaList)
        myrv.layoutManager = GridLayoutManager(this, 3)
        myrv.adapter = myAdapter
    }


    private fun loadNewManga() {
//        refresh.isRefreshing = true
        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(1).toList()
                mangaList.addAll(list)
                runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                }
            }
        }
    }
