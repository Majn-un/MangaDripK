package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FragRanked : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()

    private val searchList = mutableListOf<MangaModel>()
    private val test = mutableListOf<MangaModel>()
    private val progressDialog = CustomProgressDialog()
    private var pageNumber = 1
    private val baseUrl = "https://www.mangahere.cc"
    lateinit var gridlayoutManager : GridLayoutManager


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
        val view : View = inflater.inflate(R.layout.fragment_all, container, false)




        activity?.let { progressDialog.show(it) }
        loadNewManga()

        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        val myrv = view.findViewById(R.id.all_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
        gridlayoutManager = GridLayoutManager(activity, 3)
        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter
        progressDialog.dialog.dismiss()


        return view
    }


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//
//        activity!!.menuInflater.inflate(R.menu.search_menu, menu)
//        val searchViewItem = menu.findItem(R.id.action_search)
//        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = searchViewItem.actionView as SearchView
//        searchView.queryHint = "Search..."
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
//        searchView.setIconifiedByDefault(false)
//        val queryTextListener: SearchView.OnQueryTextListener =
//            object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(s: String): Boolean {
//                    val list = Sources.MANGA_HERE.search(s)
//
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
//                    var newText = newText
//                    newText = newText.toLowerCase()
////                    println(obj)
//                    val newList: ArrayList<MangaModel> = ArrayList<MangaModel>()
//                    for (manga in mangaList) {
//                        val title: String = manga.title.toLowerCase()
//                        if (title.contains(newText)) {
//                            newList.add(manga)
//                        }
//                    }
//                    myAdapter?.setFilter(newList)
//                    return false
//                }
//            }
//        searchView.setOnQueryTextListener(queryTextListener)
//    }

    private fun loadNewManga() {

        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getMangaRanked(1).toList()
                println(pageNumber)
                mangaList.addAll(list)
                println(list.size)
                activity!!.runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}