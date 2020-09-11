package com.example.mangadripk.Activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
import java.util.*


class UpdateActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    lateinit var gridlayoutManager: GridLayoutManager
    private val chapterList = mutableListOf<MangaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_update) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        toolbar.title = "New Updates";
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        val Chapter_List = intent.extras!!.getString("list")

        val aList: ArrayList<*> = ArrayList<Any?>(
            Arrays.asList(
                *Chapter_List!!.split(",".toRegex()).toTypedArray()
            )
        )
        for (i in aList.indices) {
            val beta = aList[i].toString().split("-")
            if (beta.size != 1) {
                val image = beta[1].replace("\\s+".toRegex(), "")
                val new = beta[2].replace("\\s+".toRegex(), "")
                val alpha = MangaModel(beta[0], "", new, image, Sources.MANGA_HERE)
                println("yerr" + alpha)
                chapterList.add(alpha)
            }
        }


        val myrv = findViewById<RecyclerView>(R.id.update_id)
        myAdapter = RecyclerViewAdapter(this, chapterList)
        myrv.layoutManager = GridLayoutManager(this, 3)

//        gridlayoutManager = Utility(this, 400).apply { orientation = GridLayoutManager.VERTICAL }
//        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu, menu)

        val searchViewItem = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchViewItem?.actionView as SearchView

        val searchEditText = searchView.findViewById<View>(R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(R.color.white))
        searchEditText.setHintTextColor(resources.getColor(R.color.white))
        searchView.queryHint = "Search Manga..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    val newText = newText.toLowerCase()
                    val newList: ArrayList<MangaModel> = ArrayList()
                    for (manga in chapterList) {
                        val title = manga.title!!.toLowerCase()
                        if (title.contains(newText)) {
                            newList.add(manga)
                        }
                    }
                    myAdapter?.setFilter(newList)
                    return false
                }
            }

        searchView.setOnQueryTextListener(queryTextListener)
        return true
    }
}