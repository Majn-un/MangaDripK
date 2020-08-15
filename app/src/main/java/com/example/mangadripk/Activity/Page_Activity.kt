package com.example.mangadripk.Activity

//import android.app.ProgressDialog

import android.R.attr.thumbnail
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.ChapterModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Page_Activity : AppCompatActivity() {
    lateinit var lstPages: MutableList<Page>
    private var myViewPager: PageViewAdapter? = null
    private var Page_Model: ChapterModel = ChapterModel("","","",Sources.MANGA_HERE)
    var myDB: RecentDB? = null
    var OG_name : String? = ""
    var recent : Recent = Recent("","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        val intent = intent
        val url = intent.getStringExtra("url")
        OG_name = intent.getStringExtra("OGT")
        val OG_thumb = intent.getStringExtra("OGN")
        val name : String? = intent.getStringExtra("name")
        val upload = intent.getStringExtra("upload")
        val uploadedTime = intent.getStringExtra("uploadtime")
        val MangaYUH = name?.let {
            if (url != null) {
                if (upload != null) {
                        Page_Model = ChapterModel(it, url,upload, Sources.MANGA_HERE)

                }
            }
        }

        recent = Recent(OG_name,name,OG_thumb,Page_Model.url)
        updateRecent()

        lstPages = ArrayList()
        mangaPages()
        val myrv = findViewById<View>(R.id.view_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myrv.adapter = myViewPager
    }

    private fun updateRecent() {
        myDB = RecentDB(this)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            if (data.getString(3) == OG_name) {
                println("Already in recents")
                return
            }
        }
        myDB!!.addData(recent)
        myDB!!.close()
    }

    private fun AddData(
        manga : Recent
    ) {
        myDB = RecentDB(this)
        val insertData = myDB!!.addData(manga)

        if (insertData == true) {
            Toast.makeText(this@Page_Activity, "Successfully Entered Data!", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this@Page_Activity, "Yuh it no work", Toast.LENGTH_LONG).show()
        }
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