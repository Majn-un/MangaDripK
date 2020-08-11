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
import com.squareup.picasso.Picasso
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
    private var cookies1: String? = null
    private var cookies2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)

        manga_status = findViewById<View>(R.id.status) as TextView
        manga_author = findViewById<View>(R.id.author) as TextView
        manga_title = findViewById<View>(R.id.depth_title) as TextView
        manga_description = findViewById<View>(R.id.depth_description) as TextView
        img = findViewById<View>(R.id.manga_thumbnail) as ImageView
        val intent = intent
        Manga_URL = intent.getStringExtra("URL")
        mangaData

        button_for_chapters =
            findViewById<View>(R.id.chapters_button) as Button
        button_for_chapters!!.setOnClickListener {
            val intent = Intent(this@Manga_Activity, Chapter_Activity::class.java)
            intent.putExtra("URL", Manga_URL)
            startActivity(intent)
        }//
    }

    private val mangaData: Unit
        private get() {
            Thread(Runnable {
                try {
//
                    val doc = Jsoup.connect(Manga_URL).get()
                    val title = doc.select("span.detail-info-right-title-font").text()
                    val status = doc.select("span.detail-info-right-title-tip").text()
                    val description = doc.select("p.fullcontent").text()
                    val author = doc.select("p.detail-info-right-say").select("a").text()
                    val imageUrl = doc.select("img.detail-info-cover-img").select("img[src^=http]").attr("abs:src")
                    setValues(description,author,status,title,imageUrl)
                } catch (ignored: InterruptedException) {
                    Log.d("Yuh", "Something is not working")
                }
            }).start()
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
            manga_author!!.text = "Author: $author"
            manga_status!!.text = "Status: $status"
            manga_title!!.text = title
            Picasso.get().load(img_URL).into(img)
        }
    }
}