package com.example.mangadripk.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mangadripk.Adapter.ChapterViewAdapter
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.ChapterModel
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.ArrayList


class Manga_Activity : AppCompatActivity() {
    private val myAdapter: RecyclerViewAdapter? = null
    private var myChapterAdapter: ChapterViewAdapter? = null
    private var chapter_title: TextView? = null
    lateinit var lstChapter: MutableList<Chapter>
    private var OG_name: String? = null
    private var OG_Thumb: String? = null
    private var button_for_chapters: Button? = null
    private var button_for_favorites: Button? = null
    private var button_for_resume: Button? = null
    private val progressDialog = CustomProgressDialog()
    var myDB: FavoriteDB? = null
    private var manga_title: TextView? = null
    private var manga_description: TextView? = null
    private var manga_status: TextView? = null
    private var master_name: String? = null

    private var manga_author: TextView? = null
    private var img: ImageView? = null
    private var Manga_URL: String? = null
    private var ShineManga: MangaModel = MangaModel("","","","",Sources.MANGA_HERE)
    private var ChapterManga: MangaModel = MangaModel("","","","",Sources.MANGA_HERE)



    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)
        progressDialog.show(this)

        manga_status = findViewById<View>(R.id.status) as TextView
        manga_author = findViewById<View>(R.id.author) as TextView
        manga_title = findViewById<View>(R.id.depth_title) as TextView
        manga_description = findViewById<View>(R.id.depth_description) as TextView
        img = findViewById<View>(R.id.manga_thumbnail) as ImageView
        val intent = intent
        Manga_URL = intent.getStringExtra("mangaUrl")
        val imgUrl = intent.getStringExtra("imgUrl")
        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        master_name = title

        val MangaYUH = title?.let {
            if (description != null) {
                if (Manga_URL != null) {
                    if (imgUrl != null) {
                        ShineManga = MangaModel(it,description, Manga_URL!!,imgUrl,Sources.MANGA_HERE)
                    }
                }
            }
        }

        MangaActivity()

//        button_for_chapters = findViewById<View>(R.id.chapters_button) as Button
//        button_for_chapters!!.setOnClickListener {
//            val intent = Intent(this@Manga_Activity, Chapter_Activity::class.java)
//            intent.putExtra("mangaUrl",Manga_URL)
//            intent.putExtra("imgUrl",imgUrl)
//            intent.putExtra("description",description)
//            intent.putExtra("title",title)
//            intent.putExtra("source",Sources.toString())
//            startActivity(intent)
//        }

        val myDB_recent = RecentDB(this)
        var found = false
        button_for_resume = findViewById<View>(R.id.resume) as Button
        button_for_resume!!.setOnClickListener {
            val data: Cursor = myDB_recent.listContents
            while (data.moveToNext()) {
                if (data.getString(3) == title) {
                    val rec = Recent(
                        data.getString(3),
                        data.getString(1),
                        data.getString(2),
                        data.getString(4),
                        data.getString(5)
                    )
                    val resume = Intent(this@Manga_Activity, Page_Activity::class.java)
                    resume.putExtra("Chapter_List", rec.chapter_link)
                    resume.putExtra("name",rec.chapter)
                    resume.putExtra("url",rec.Link)
                    resume.putExtra("uploadedtime","uploadedtime")
                    resume.putExtra("upload","upload")
                    resume.putExtra("source", Sources.MANGA_HERE.toString())
                    resume.putExtra("OGN",rec.thumbnail)
                    resume.putExtra("OGT",rec.title)
                    found = true
                    startActivity(resume)
                }
            }

            if (!found) {
                Toast.makeText(this, "You haven't started it bruv", Toast.LENGTH_SHORT).show()
            }
        }

        button_for_favorites = findViewById<View>(R.id.favorite_button) as Button
        myDB = FavoriteDB(this)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            if (data.getString(2) == Manga_URL) {
                button_for_favorites!!.setBackgroundResource(R.drawable.ic_baseline_favorite_green_24)
            }
        }
        button_for_favorites!!.setOnClickListener {
            if(button_for_favorites!!.background.constantState == ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_favorite_24, null)?.constantState) {
                updateFavorite()
                button_for_favorites!!.setBackgroundResource(R.drawable.ic_baseline_favorite_green_24)
            } else if (button_for_favorites!!.background.constantState == ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_favorite_green_24, null)?.constantState){
                master_name?.let { it1 -> myDB!!.deleteData(it1) }
                Toast.makeText(this, "Unfavorited", Toast.LENGTH_SHORT ).show()
                button_for_favorites!!.setBackgroundResource(R.drawable.ic_baseline_favorite_24)

            }

        }

        val MangaYer = title?.let {
            if (description != null) {
                if (Manga_URL != null) {
                    if (imgUrl != null) {
                        ChapterManga = MangaModel(it,description, Manga_URL!!,imgUrl, Sources.MANGA_HERE)
                    }
                }
            }
        }


        ChapterActivity()
        lstChapter = ArrayList()
        val mycrv = findViewById<View>(R.id.chapter_recycler) as RecyclerView
        myChapterAdapter = ChapterViewAdapter(this, lstChapter)
        mycrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mycrv.adapter = myChapterAdapter

//        refreshLayout.setOnRefreshListener {
//            progressDialog.show(this)
//            lstChapter.clear()
//            ChapterActivity()
//            myChapterAdapter = ChapterViewAdapter(this, lstChapter)
//            myrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//            myrv.adapter = myAdapter
//            refreshLayout.isRefreshing = false
//        }

    }


    private fun updateFavorite() {
        myDB = FavoriteDB(this)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            if (data.getString(2) == Manga_URL) {
                Toast.makeText(this, "Already Favorited", Toast.LENGTH_SHORT).show()
                return
            }
        }
        myDB!!.addData(ShineManga)
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
        myDB!!.close()
    }

    private fun AddData(
        manga : MangaModel
    ) {
        myDB = FavoriteDB(this)
        val insertData = myDB!!.addData(manga)

        if (insertData == true) {
            Toast.makeText(this@Manga_Activity, "Successfully Entered Data!", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this@Manga_Activity, "Yuh it no work", Toast.LENGTH_LONG).show()
        }
    }

    private fun MangaActivity() {
        GlobalScope.launch {
            try {
                val mangaActivity = ShineManga.toInfoModel()
                setValues(mangaActivity.description,mangaActivity.author,mangaActivity.status,ShineManga.title,ShineManga.imageUrl)
                println(mangaActivity.status)
                runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }
                progressDialog.dialog.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun ChapterActivity() {
        GlobalScope.launch {
            try {
                val mangaActivity = ChapterManga.toInfoModel()

                for (item in mangaActivity.chapters) {
//                    val Page_Model = ChapterModel(item.url, item.url, "upload", Sources.MANGA_HERE)
//                    val image_link = Page_Model.getFirstImage().pages[0]
//                    println(image_link)
                    lstChapter.add(Chapter(item.name,item.url,item.sources,"2",item.uploadedTime, OG_Thumb,OG_name))
                }

                runOnUiThread {
                    myChapterAdapter?.notifyDataSetChanged()
                }
//                progressDialog.dialog.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun setValues(
        description: String,
        author: String,
        status: String,
        title: String,
        img_URL: String
    ) {
        runOnUiThread {
            manga_description!!.text = description
            manga_author!!.text = author
            manga_status!!.text = "Status: $status"
            manga_title!!.text = title
            img?.let { Glide.with(this).load(img_URL).dontTransform().into(it) }

        }
    }
}