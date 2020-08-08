package com.example.mangadripk

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    lateinit var lstManga: MutableList<Manga>
    var cookie: Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lstManga = ArrayList()
        getWebsite()

        val myrv = findViewById<View>(R.id.recyclerview_id) as RecyclerView
        myAdapter = RecyclerViewAdapter(this, lstManga)
        myrv.layoutManager = GridLayoutManager(this, 3)
        myrv.adapter = myAdapter
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.toolbar_item, menu)
//        val searchViewItem = menu.findItem(R.id.action_search)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = searchViewItem.actionView as SearchView
//        searchView.queryHint = "Search..."
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.setIconifiedByDefault(false)
//        val queryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(s: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                var newText = newText
//                newText = newText.toLowerCase()
//                val newList: ArrayList<Manga> = ArrayList<Manga>()
//                for (manga in lstManga!!) {
//                    val title: String = manga.getTitle().toLowerCase()
//                    if (title.contains(newText)) {
//                        newList.add(manga)
//                    }
//                }
//                myAdapter.setFilter(newList)
//                return true
//            }
//        }
//        searchView.setOnQueryTextListener(queryTextListener)
//        return true
//    }

    private fun getWebsite() {
        Thread(Runnable {
            try {
                val rand = Random()
                val n = rand.nextInt(2000)
                Thread.sleep(n.toLong())
                val res: Connection.Response = Jsoup
                        .connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                        .method(Connection.Method.POST)
                        .execute()
                cookie = res.cookies()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            for (k in 0..0) {
                try {
                    val rand = Random()
                    val n = rand.nextInt(2000)
                    Thread.sleep(n.toLong())
                    val doc: Document = Jsoup.connect("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                            .cookies(cookie)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                            .referrer("https://mangakakalot.com/manga_list?type=topview&category=all&state=all&page=1")
                            .get()
                    val description: Elements = doc.select("div.list-truyen-item-wrap")
                    val length: Int = description.size
                    for (i in 0 until length) {
                        val title: String = description.eq(i).select("a").attr("title")
                        val imgUrl: String = description.eq(i).select("a").select("img").attr("src")
                        val MangaLink: String = description.eq(i).select("a").eq(1).attr("abs:href")
//                        var MangaLink = ""
//                        for (m in 0 until int_MangaLink) {
//                            MangaLink += description.eq(i).select("a").eq(1).attr("abs:href").charAt(m) ///
//                        }
                        val test = Manga(title, MangaLink, imgUrl)
                        lstManga.add(test)
                    }
                } catch (ignored: IOException) {
                    Log.d("Yuh", "Something is not working") ////
                }
                runOnUiThread { myAdapter?.notifyDataSetChanged() }
            }
        }).start()
    }

}