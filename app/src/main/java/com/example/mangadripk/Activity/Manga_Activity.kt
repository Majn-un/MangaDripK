package com.example.mangadripk.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.R

import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.w3c.dom.Document
import java.io.IOException
import java.util.*
import java.util.regex.Pattern


class Manga_Activity : AppCompatActivity() {
    private val myAdapter: RecyclerViewAdapter? = null
    private var button_for_chapters: Button? = null

    //    var refreshLayout: SwipeRefreshLayout? = null
    private var manga_title: TextView? = null
    private var manga_description: TextView? = null
    private var manga_status: TextView? = null
    private var manga_author: TextView? = null
    private var img: ImageView? = null
    private var Manga_URL: String? = null
    private var ShineManga: MangaModel = MangaModel("","","","",Sources.MANGA_HERE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)

        manga_status = findViewById<View>(R.id.status) as TextView
//        manga_author = findViewById<View>(R.id.author) as TextView
        manga_title = findViewById<View>(R.id.depth_title) as TextView
        manga_description = findViewById<View>(R.id.depth_description) as TextView
        img = findViewById<View>(R.id.manga_thumbnail) as ImageView
        val intent = intent
        val mangaUrl = intent.getStringExtra("mangaUrl")
        val imgUrl = intent.getStringExtra("imgUrl")
        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")
        val source = intent.getStringExtra("source")


        val MangaYUH = title?.let {
            if (description != null) {
                if (mangaUrl != null) {
                    if (imgUrl != null) {
                        ShineManga = MangaModel(it,description,mangaUrl,imgUrl,Sources.MANGA_HERE)
//                        println(ShineManga.title)

                    }
                }
            }
        }

        MangaActivity()
//        mangaData

        button_for_chapters = findViewById<View>(R.id.chapters_button) as Button
        button_for_chapters!!.setOnClickListener {

            val intent = Intent(this@Manga_Activity, Chapter_Activity::class.java)
            intent.putExtra("mangaUrl",mangaUrl)
            intent.putExtra("imgUrl",imgUrl)
            intent.putExtra("description",description)
            intent.putExtra("title",title)
            intent.putExtra("source",Sources.toString())
            startActivity(intent)
        }//
    }

    private fun MangaActivity() {
//        refresh.isRefreshing = true
        GlobalScope.launch {
            try {
                val mangaActivity = ShineManga.toInfoModel()
                setValues(ShineManga.description,"","",ShineManga.title,ShineManga.imageUrl)
//                val Pages = mangaActivity.chapters[1].getPageInfo()
//                println("YUH"+Pages)

//                mangaList.addAll(list)
//                println(list[1])
                runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
//                    adapter2.addItems(list)
//                    search_layout.suffixText = "${mangaList.size}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
//                FirebaseCrashlytics.getInstance().log("$currentSource had an error")
//                FirebaseCrashlytics.getInstance().recordException(e)
//                Firebase.analytics.logEvent("manga_load_error") {
//                    param(FirebaseAnalytics.Param.ITEM_NAME, currentSource.name)
            }
//                runOnUiThread {
//                    MaterialAlertDialogBuilder(this@MainActivity)
//                        .setTitle(R.string.wentWrong)
//                        .setMessage(getString(R.string.wentWrongInfo, currentSource.name))
//                        .setPositiveButton(R.string.ok) { d, _ -> d.dismiss() }
//                        .show()
//                }
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
//            manga_author!!.text = "Author: "
//            manga_status!!.text = "Status: "
            manga_title!!.text = title
            Picasso.get().load(img_URL).into(img)
        }
    }
}