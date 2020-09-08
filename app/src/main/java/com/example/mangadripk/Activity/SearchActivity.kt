package com.example.mangadripk.Activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


class SearchActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()
    private val baseUrl = "https://www.mangahere.cc"
    lateinit var gridlayoutManager: GridLayoutManager
    private val progressDialog = CustomProgressDialog()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        toolbar.title = "";
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myrv = findViewById<RecyclerView>(R.id.search_id)
        myAdapter = RecyclerViewAdapter(this, mangaList)
        gridlayoutManager = Utility(this, 400).apply { orientation = GridLayoutManager.VERTICAL }
        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)

        val searchViewItem = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchViewItem?.actionView as SearchView
        searchView.isFocusable = true;
        searchView.isIconified = false

        val searchEditText = searchView.findViewById<View>(R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(R.color.white))
        searchEditText.setHintTextColor(resources.getColor(R.color.white))
        searchView.queryHint = "Search Manga..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(s: String): Boolean {
                    Thread.sleep(500)
//                    progressDialog.show(this@SearchActivity)
                    mangaList.clear()
                    SearchMangaHere(s)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
//                    val list = SearchMangaHere(newText)
                    return false
                }
            }

        searchView.setOnQueryTextListener(queryTextListener)
        return true
    }

    private fun SearchMangaHere(search_item: String) {
        GlobalScope.launch {
            try {
                var i = 1
                while (i < 3) {
                    val desiredManga = search_item.replace(" ", "+")
                    val url = "https://www.mangahere.cc/search?page=$i&title=$desiredManga&genres=&nogenres=&sort=&stype=1&name=&type=0&author_method=cw&author=&artist_method=cw&artist=&rating_method=eq&rating=&released_method=eq&released=&st=0"
                    val res = Jsoup.connect(url).get().select(".manga-list-4-list > li")
                        .map {
                            MangaModel(
                                title = it.select("a").first().attr("title"),
                                description = it.select("p.manga-list-4-item-tip").last().text(),
                                mangaUrl = "${baseUrl}${
                                    it.select(".manga-list-4-item-title > a")
                                        .first().attr("href")
                                }",
                                imageUrl = it.select("img.manga-list-4-cover").first()
                                    .attr("abs:src"),
                                source = Sources.MANGA_HERE
                            )
                        }

                    filterGayShit(res)
                i++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun filterGayShit(list: List<MangaModel>) {
        for (item in list) {
            val tagList: MutableList<String> = mutableListOf<String>()
            Thread.sleep(200)
            val length = Jsoup.connect(item.mangaUrl).get().select("p.detail-info-right-tag-list").select(
                "a"
            ).size
            for (i in 0..length-1) {
                val res = Jsoup.connect(item.mangaUrl).get().select("p.detail-info-right-tag-list").select(
                    "a"
                ).eq(i).attr("title")
                tagList.add(res)
            }
            if (tagList.any{it == "Doujinshi" || it == "Yuri" || it == "Yaoi"}) {
                println(item.title + " you really think you slick....thats gay")
            } else {
                if (mangaList.contains(item)) {
                    println("already here")
                } else {
                    mangaList.add(item)
                }
            }
            runOnUiThread {
                myAdapter?.notifyDataSetChanged()
            }
        }
    }
}