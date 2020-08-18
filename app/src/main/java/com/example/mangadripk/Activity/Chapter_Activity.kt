package com.example.mangadripk.Activity

import com.example.mangadripk.R
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.example.mangadripk.Adapter.ChapterViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.*



class Chapter_Activity : AppCompatActivity() {
    private var myAdapter: ChapterViewAdapter? = null
    private var chapter_title: TextView? = null
    lateinit var lstChapter: MutableList<Chapter>
    private var OG_name: String? = null
    private var OG_Thumb: String? = null
    private val progressDialog = CustomProgressDialog()

    private var ChapterManga: MangaModel = MangaModel("","","","",Sources.MANGA_HERE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)
        progressDialog.show(this)

        chapter_title = findViewById<View>(R.id.chapter_title) as? TextView
        val intent = intent
        val mangaUrl = intent.getStringExtra("mangaUrl")
        val imgUrl = intent.getStringExtra("imgUrl")
        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        OG_name = intent.getStringExtra("title")
        OG_Thumb = intent.getStringExtra("imgUrl")

        val MangaYUH = title?.let {
            if (description != null) {
                if (mangaUrl != null) {
                    if (imgUrl != null) {
                        ChapterManga = MangaModel(it,description,mangaUrl,imgUrl, Sources.MANGA_HERE)
                    }
                }
            }
        }
        ChapterActivity()
        lstChapter = ArrayList()
        val myrv = findViewById<View>(R.id.chapter_recycler) as RecyclerView
        myAdapter = ChapterViewAdapter(this, lstChapter)
        myrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myrv.adapter = myAdapter

        refreshLayout.setOnRefreshListener {
            progressDialog.show(this)
            lstChapter.clear()
            ChapterActivity()
            myAdapter = ChapterViewAdapter(this, lstChapter)
            myrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            myrv.adapter = myAdapter
            refreshLayout.isRefreshing = false
        }

    }


    private fun ChapterActivity() {
        GlobalScope.launch {
            try {
                val mangaActivity = ChapterManga.toInfoModel()
                for (item in mangaActivity.chapters) {
                    lstChapter.add(Chapter(item.name,item.url,item.sources,"2",item.uploadedTime, OG_Thumb,OG_name))
                }

                runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }
                progressDialog.dialog.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}