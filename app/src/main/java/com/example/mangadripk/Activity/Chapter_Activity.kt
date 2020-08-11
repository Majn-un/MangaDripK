package com.example.mangadripk.Activity

import com.example.mangadripk.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

import com.example.mangadripk.Adapter.ChapterViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.Manga
import org.jsoup.Jsoup
import java.io.IOException
import java.util.*



class Chapter_Activity : AppCompatActivity() {
    private var myAdapter: ChapterViewAdapter? = null
    private var chapter_title: TextView? = null
    lateinit var lstChapter: MutableList<Chapter>
    private var Manga_URL: String? = null
//    var refreshLayout: SwipeRefreshLayout? = null
    private var Cookie1: String? = null
    private var Cookie2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        chapter_title = findViewById<View>(R.id.chapter_title) as? TextView
        val intent = intent
        Manga_URL = intent.getStringExtra("URL")
//        Cookie2 = intent.getStringExtra("ci_session")
//        Cookie1 = intent.getStringExtra("__cfduid")
        lstChapter = ArrayList()
        chapters()
        val myrv = findViewById<View>(R.id.chapter_recycler) as RecyclerView
        myAdapter = ChapterViewAdapter(this, lstChapter)
        myrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        myrv.adapter = myAdapter
//        refreshLayout.setOnRefreshListener(OnRefreshListener {
//            chapters
//            myAdapter = ChapterViewAdapter(this@Chapter_Activity, lstChapter)
//            myrv.layoutManager = LinearLayoutManager(
//                this@Chapter_Activity,
//                LinearLayoutManager.VERTICAL,
//                false
//            )
//            myrv.adapter = myAdapter
//            refreshLayout.setRefreshing(false)
//        })
    }//

    //                            .cookies(cookies)
    private fun chapters() {
            Thread(Runnable {
                try {
//                    val cookies = LinkedHashMap<String, String?>()
//                    cookies["__cfduid"] = Cookie1
//                    cookies["ci_session"] = Cookie2
//                    Log.d("cookes", cookies.toString() + "")
//                    val rand = Random()
//                    val n = rand.nextInt(2000)
//                    Thread.sleep(n.toLong())
                    val doc = Jsoup.connect(Manga_URL).get()
                    val description = doc.select("div[id=chapterlist]").select("ul.detail-main-list").select("li")
                    val length = description.size
                    for (i in 0 until length) {
                        val Link = description.eq(i).select("a").attr("abs:href")
                        val Chapter_Title = description.eq(i).select("a").select("p.title3").text()
                        val Date = description.eq(1).select("a").select("p.title2").text()
                        val chap = Chapter(Chapter_Title, Link)
                        lstChapter.add(chap)
                    }
//                    progressDialog!!.dismiss()
                } catch (ignored: IOException) {
                    Log.d("Yuh", "Something is not working")
                }
                runOnUiThread { myAdapter?.notifyDataSetChanged() }
        }).start()
    }


}