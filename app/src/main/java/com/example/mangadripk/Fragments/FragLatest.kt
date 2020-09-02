package com.example.mangadripk.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class FragLatest : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private var mangaList = mutableListOf<MangaModel>()

    private val searchList = mutableListOf<MangaModel>()
    private val test = mutableListOf<MangaModel>()
    private val progressDialog = CustomProgressDialog()
    private var pageNumber = 1
    private val baseUrl = "https://www.mangahere.cc"
    lateinit var gridlayoutManager : GridLayoutManager
    lateinit var refreshLayout: SwipeRefreshLayout
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
        val view : View = inflater.inflate(R.layout.fragment_latest, container, false)
        refreshLayout = view.findViewById(R.id.refreshLatest)

        activity?.let { progressDialog.show(it) }
        loadNewManga()

        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        val myrv = view.findViewById(R.id.latest_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
        gridlayoutManager = GridLayoutManager(activity, 3)
        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter
        refreshLayout.setOnRefreshListener(OnRefreshListener {
            mangaList = mutableListOf<MangaModel>()
            pageNumber = 1
            loadNewManga()
            myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
            gridlayoutManager = GridLayoutManager(activity, 3)
            myrv.layoutManager = gridlayoutManager
            myrv.adapter = myAdapter
            refreshLayout.isRefreshing = false

        })
        myrv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadNewManga()
                }
            }
        })
        progressDialog.dialog.dismiss()

        return view
    }




    private fun loadNewManga() {

        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getMangaLatest(pageNumber++).toList()
                for (manga in list) {
                    if (manga.imageUrl != "") {
                        mangaList.add(manga)
                        println(manga)
                    }
                }
                activity!!.runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}