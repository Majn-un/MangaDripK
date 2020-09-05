package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Activity.Manga_Activity
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


class FragAll : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()
    private val response = mutableListOf<MangaModel>()
    private var res = mutableListOf<MangaModel>()
    private val searchList = mutableListOf<MangaModel>()
    private val test = mutableListOf<MangaModel>()
    private val progressDialog = CustomProgressDialog()
    private var pageNumber = 1
    private var searchNumber = 1
    private val baseUrl = "https://www.mangahere.cc"
    lateinit var gridlayoutManager: GridLayoutManager

    var myFragment: View? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null


    fun HomeFragment() {
        // Required empty public constructor
    }

    fun getInstance(): Library? {
        return Library()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true)
        val view: View = inflater.inflate(R.layout.fragment_all, container, false)





//        activity?.let { progressDialog.show(it) }

        loadNewManga()


        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        val myrv = view.findViewById(R.id.all_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
        gridlayoutManager =
            Utility(activity, 400).apply { orientation = GridLayoutManager.VERTICAL }
        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter
        myrv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadNewManga()

                }
            }
        })
        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        activity!!.menuInflater.inflate(R.menu.search_menu, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchViewItem.actionView as SearchView

        searchView.queryHint = "Search..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.setIconifiedByDefault(false)

        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(s: String): Boolean {
                    Thread.sleep(500)
                    val list = SearchMangaHere(s + " ")
                    val newList: ArrayList<MangaModel> = ArrayList<MangaModel>()
                    if (list != null) {
                        for (manga in list) {
                            newList.add(manga)
                        }
                    }
                    response.clear()
                    myAdapter?.setFilter(newList)

                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
//                    val list = SearchMangaHere(newText)
                    return false
                }
            }
        searchView.setOnQueryTextFocusChangeListener { _, newViewFocus ->
            if (!newViewFocus) {
                searchViewItem.collapseActionView()
                val myrv = view?.findViewById(R.id.all_id) as RecyclerView
                myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
                gridlayoutManager = Utility(activity, 400).apply { orientation = GridLayoutManager.VERTICAL }
                myrv.layoutManager = gridlayoutManager
                myrv.adapter = myAdapter
            }
        }
        searchView.setOnQueryTextListener(queryTextListener)
    }



    private fun SearchMangaHere(search_item: String): List<MangaModel>? {

        GlobalScope.launch {
            try {
                var i = 1
                var check = true
                while (check == true) {
                    val desiredManga = search_item.replace(" ", "+")
                    val url =
                        "https://www.mangahere.cc/search?page=$i&title=$desiredManga&genres=&nogenres=&sort=&stype=1&name=&type=0&author_method=cw&author=&artist_method=cw&artist=&rating_method=eq&rating=&released_method=eq&released=&st=0"
                    println(url)
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


                    for (item in res) {
                        println(item)
                        response.add(item)
                    }
                    if (res.isEmpty()) {
                        check = false
                        break
                    }
                    i++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return response

    }

    private fun loadNewManga() {
        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(pageNumber++).toList()
                mangaList.addAll(list)
                activity!!.runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}