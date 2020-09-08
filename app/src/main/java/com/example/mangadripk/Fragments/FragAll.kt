package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Activity.Manga_Activity
import com.example.mangadripk.Activity.SearchActivity
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
import kotlinx.coroutines.GlobalScope
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
        requireActivity().menuInflater.inflate(R.menu.menu, menu)

//
//        activity!!.menuInflater.inflate(R.menu.search_menu, menu)
//        val searchViewItem = menu.findItem(R.id.action_search)
//        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = searchViewItem.actionView as SearchView
//        val searchEditText =
//            searchView.findViewById<View>(R.id.search_src_text) as EditText
//        searchEditText.setTextColor(resources.getColor(R.color.white))
//        searchEditText.setHintTextColor(resources.getColor(R.color.white))
//        searchView.queryHint = "Search Manga..."
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
//        searchView.setIconifiedByDefault(false)
//
//        val queryTextListener: SearchView.OnQueryTextListener =
//            object : SearchView.OnQueryTextListener {
//
//                override fun onQueryTextSubmit(s: String): Boolean {
//                    Thread.sleep(500)
//                    SearchMangaHere(s)
////                    println("in search " + list)
////                    val newList: ArrayList<MangaModel> = ArrayList<MangaModel>()
////                    if (list != null) {
////                        for (manga in list) {
////                            newList.add(manga)
////                        }
////                    }
////                    println("in newList - " + newList)
//                    response.clear()
//
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
////                    val list = SearchMangaHere(newText)
//                    return false
//                }
//            }
//        searchView.setOnQueryTextFocusChangeListener { _, newViewFocus ->
//            if (!newViewFocus) {
//                searchViewItem.collapseActionView()
//                val myrv = view?.findViewById(R.id.all_id) as RecyclerView
//                myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
//                gridlayoutManager = Utility(activity, 400).apply { orientation = GridLayoutManager.VERTICAL }
//                myrv.layoutManager = gridlayoutManager
//                myrv.adapter = myAdapter
//            }
//        }
//        searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.Search1) {
            val intent = Intent(activity, SearchActivity::class.java)
            activity?.startActivity(intent)
        }
        return true
    }



    private fun loadNewManga() {
        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(pageNumber++).toList()
                mangaList.addAll(list)
                requireActivity().runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}