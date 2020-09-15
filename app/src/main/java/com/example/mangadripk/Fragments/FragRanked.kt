package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Database.Source
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
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
    private var list : List<MangaModel> = listOf()
    private var source: String? = null
    var myDB: Source? = null
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




        myDB = Source(activity)

        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            source = data.getString(1)
        }

        myDB!!.close()



        setHasOptionsMenu(true)
        val view : View = inflater.inflate(R.layout.fragment_all, container, false)




        activity?.let { progressDialog.show(it) }
        loadNewManga()

        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        val myrv = view.findViewById(R.id.all_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
//        gridlayoutManager = Utility(activity, 400).apply { orientation = GridLayoutManager.VERTICAL }
//        myrv.layoutManager = gridlayoutManager
        myrv.layoutManager = GridLayoutManager(context, 3)

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
                if (source == "MangaFourLife") {
//                    list = Sources.MANGA_4_LIFE.getMangaRanked(pageNumber++).toList()
                    Toast.makeText(activity, "Not Available Currently", Toast.LENGTH_SHORT).show()
                } else if (source == "MangaHere") {
                    list = Sources.MANGA_HERE.getMangaRanked(pageNumber++).toList()
                } else if (source == "NineAnime") {
                    list = Sources.NINE_ANIME.getMangaRanked(pageNumber++).toList()
                }
//                } else if (source == "MangaPark") {
//                    Toast.makeText(activity, "Not Available Currently", Toast.LENGTH_SHORT).show()
////                    list = Sources.MANGA_PARK.getMangaRanked(pageNumber++).toList()
//                }
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