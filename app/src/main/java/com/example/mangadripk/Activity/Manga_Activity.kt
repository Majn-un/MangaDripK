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
            intent.putExtra("__cfduid", cookies1)
            intent.putExtra("ci_session", cookies2)
            startActivity(intent)
        }//
    }

    private val mangaData: Unit
        private get() {
            Thread(Runnable {
                try {
//                    val rand = Random()
//                    val n = rand.nextInt(2000)
//                    Thread.sleep(n.toLong())
//                    val res = Jsoup
//                        .connect(Manga_URL)
//                        .method(Connection.Method.POST)
//                        .execute()
//                    cookies1 = res.cookie("__cfduid")
//                    cookies2 = res.cookie("ci_session")
//                    val cookies =
//                        res.cookies()
//                    Log.d("Manga cookies", cookies.toString() + "")
                    val doc = Jsoup.connect(Manga_URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                        .referrer(Manga_URL)
                        .get()
                    val Manganelo =
                        Pattern.compile("//manganelo.com/")
                    val Mangakakalot =
                        Pattern.compile("//mangakakalot.com/")
                    val Manganelo_Searcher = Manganelo.matcher(Manga_URL)
                    val Mangakaklot_Searcher =
                        Mangakakalot.matcher(Manga_URL)
                    if (Manganelo_Searcher.find()) {
                        val author = doc.select("td.table-value").eq(1).text()
                        val status = doc.select("td.table-value").eq(2).text()
                        val description =
                            doc.select("div.panel-story-info-description").text()
                        val img_URL =
                            doc.select("span.info-image").select("img").attr("src")
                        val title =
                            doc.select("div.story-info-right").select("h1").text()
                        setValues(description, author, status, title, img_URL)
                        Log.d("Yuh", description+author+status+title+img_URL)
                    } else if (Mangakaklot_Searcher.find()) {
                        val author_2 =
                            doc.select("ul.manga-info-text").select("li").eq(1).select("a").eq(0)
                                .text() + " " + doc.select("ul.manga-info-text").select("li").eq(1)
                                .select("a").eq(1).text()
                        val status_2 =
                            doc.select("ul.manga-info-text").select("li").eq(2).text()
                        val description_2 = doc.select("div#noidungm").text()
                        val title_2 =
                            doc.select("ul.manga-info-text").select("h1").eq(0).text()
                        val img_URL =
                            doc.select("div.manga-info-pic").select("img").attr("src")
                        setValues(description_2, author_2, status_2, title_2, img_URL)
                        Log.d("Yuh", description_2+author_2+status_2+title_2+img_URL)

                    } else {
                        Log.d("Not Found", "Could not find the source")
                    }
                } catch (ignored: IOException) {
                    Log.d("Yuh", "Something is not working")
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