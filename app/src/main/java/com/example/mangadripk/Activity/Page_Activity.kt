package com.example.mangadripk.Activity

//import android.app.ProgressDialog
import com.example.mangadripk.R

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.Page
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class Page_Activity : AppCompatActivity() {
    lateinit var lstPages: MutableList<Page>
    private val chapter_title: TextView? = null
    private var Chapter_URL: String? = null
    private var myViewPager: PageViewAdapter? = null
    private var Cookie1: String? = null
    private var Cookie2: String? = null
    private val doc: Document? = null
//    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
//        progressDialog = ProgressDialog(this@Page_Activity)
//        progressDialog!!.show()
//        progressDialog.setContentView(R.layout.progress_dialog)
//        progressDialog!!.setCancelable(false)
//        progressDialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        val intent = intent
        val Title = intent.getStringExtra("Name")
        Chapter_URL = intent.getStringExtra("Link")
        Cookie1 = intent.getStringExtra("ci_session")
        Cookie2 = intent.getStringExtra("__cfduid")
        lstPages = ArrayList()
        mangaPages()
        val myrv = findViewById<View>(R.id.view_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myrv.adapter = myViewPager
    }

    //                            .cookies(cookies)
    private fun mangaPages() {
            Thread(Runnable {
                try {
//                    val cookies =
//                        LinkedHashMap<String, String?>()
//                    cookies["__cfduid"] = Cookie2
//                    cookies["ci_session"] = Cookie1
//                    Log.d("page", cookies.toString() + "")
                    val doc =
                        Jsoup.connect(Chapter_URL) //                            .cookies(cookies)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                            .referrer(Chapter_URL)
                            .get()
                    val link_list =
                        doc.select("div.container-chapter-reader").select("img")
                    val length = link_list.size
                    val pattern = "\\//(.*?)\\/"
                    if (length == 0) {
                        val Mangakakalot =
                            doc.select("div.vung-doc").select("img")
                        Log.d("Manga", Mangakakalot.toString() + "")
                        for (i in Mangakakalot.indices) {
                            val image_url = Mangakakalot.eq(i).attr("src")
                            Log.d("Mangakakalot", image_url)
                            val reincarnatedURL =
                                image_url.replace(pattern.toRegex(), "//s8.mkklcdnv8.com/")
                            val Page_Number = (i + 1).toString()
//                            Log.d("Page Link", reincarnatedURL)
                            lstPages.add(Page(reincarnatedURL, Page_Number))
                        }
                    } else {
                        val Manganelo =
                            doc.select("div.container-chapter-reader").select("img")
                        for (i in Manganelo.indices) {
                            val image_url = Manganelo.eq(i).attr("src")
                            val reincarnatedURL =
                                image_url.replace(pattern.toRegex(), "//s8.mkklcdnv8.com/")
                            Log.d("manganelo", image_url)
                            val Page_Number = (i + 1).toString()
//                            Log.d("Page Link", reincarnatedURL)

                            lstPages.add(Page(reincarnatedURL, Page_Number))
                        }
                    }
//                    progressDialog!!.dismiss()
                } catch (ignored: IOException) {
                    Log.d("Yuh", "Something is not working")
                }
                runOnUiThread { myViewPager?.notifyDataSetChanged() }
            }).start()
        }
}