package com.example.mangadripk.Activity

//import android.app.ProgressDialog
import android.database.Cursor
import com.example.mangadripk.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.Database.RecentDB

import com.example.mangadripk.Sources.Sources
import com.example.mangadripk.Sources.manga.MangaHere.extractSecretKey
import com.programmersbox.manga_sources.mangasources.ChapterModel
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.manga_sources.mangasources.PageModel
import com.squareup.duktape.Duktape
import com.squareup.okhttp.OkHttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class Page_Activity : AppCompatActivity() {
    lateinit var lstPages: MutableList<Page>
    private var myViewPager: PageViewAdapter? = null
    private var Page_Model: ChapterModel = ChapterModel("","","",Sources.MANGA_HERE)
    var myDB: RecentDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        val intent = intent
        val url = intent.getStringExtra("url")
        val OG_name = intent.getStringExtra("OGT")
        val OG_thumb = intent.getStringExtra("OGN")
        val name : String? = intent.getStringExtra("name")
        val upload = intent.getStringExtra("upload")
        val uploadedTime = intent.getStringExtra("uploadtime")
        val MangaYUH = name?.let {
            if (url != null) {
                if (upload != null) {
                        Page_Model = ChapterModel(it,url,upload, Sources.MANGA_HERE)

                }
            }
        }

        val recent = Recent(OG_name,name,OG_thumb,Page_Model.url)
//        println("debugging2"+OG_thumb)
//        println("debugging2"+OG_name)
//        println("debugging2"+Page_Model.url)
//        println("debugging2"+name)
        myDB = RecentDB(this)
        val data: Cursor = myDB!!.listContents
        var found = false
        if (myDB!!.isEmpty() == 0) {
            println("First Entry")
            AddData(recent)
        } else {
            while (data.moveToNext()) {
                println(data.getString(2))
                if (data.getString(2) == name) {
                    println("Already a favorite")
                    found = true
                    break
//                        Toast.makeText(this@Manga_Activity, "Already a favorite", Toast.LENGTH_LONG).show()
                }

            }

            if (found == false) {
                println("YUUH")
                AddData(recent)
            }
//            println(data)
            myDB!!.close()
        }

        lstPages = ArrayList()
        mangaPages()
        val myrv = findViewById<View>(R.id.view_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myrv.adapter = myViewPager
    }

    private fun AddData(
        manga : Recent
    ) {
        println("debugging"+manga.thumbnail)
        println("debugging"+manga.title)
        println("debugging"+manga.Link)
        println("debugging"+manga.chapter)


        val insertData = myDB!!.addData(manga)
    }

    private fun mangaPages() {
        GlobalScope.launch {
            try {
                val mangaActivity = Page_Model.getPageInfo()
                for (i in 0 until mangaActivity.pages.size) {
                    val page = Page(mangaActivity.pages[i],(i+1).toString())
                    lstPages.add(page)
                }
                runOnUiThread {
                    myViewPager?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}