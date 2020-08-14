package com.example.mangadripk.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.R
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()
//    var cookie: Map<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNewManga()

        val myrv = findViewById<View>(R.id.recyclerview_id) as RecyclerView
        myAdapter = RecyclerViewAdapter(this, mangaList)
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
//                    val title: String? = manga.title?.toLowerCase()
//                    if (title.contains(newText)) {
//                        newList.add(manga)
//                    }
//                }
//                myAdapter?.setFilter(newList)
//                return true
//            }
//        }
//        searchView.setOnQueryTextListener(queryTextListener)
//        return true
//    }

    private fun loadNewManga() {
//        refresh.isRefreshing = true
        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(1).toList()
                mangaList.addAll(list)
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
    }
