package com.example.mangadripk.Fragments

import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.Database.Source
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.example.mangadripk.Sources.manga.MangaFourLife
import com.example.mangadripk.Sources.manga.Mangamutiny
import com.google.android.material.tabs.TabLayout
import com.programmersbox.gsonutils.fromJson
import com.programmersbox.gsonutils.getJsonApi
import com.programmersbox.manga_sources.mangasources.MangaModel
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
    private var list: List<MangaModel> = listOf()
    private val baseUrl = "https://www.mangahere.cc"
    lateinit var gridlayoutManager: GridLayoutManager
    var myFragment: View? = null
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    var myDB: Source? = null
    private var source: String? = null


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


        myDB = Source(activity)

        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            source = data.getString(1)
        }

        myDB!!.close()
        loadNewManga()

        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);

        val myrv = view.findViewById(R.id.all_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
//        gridlayoutManager = Utility(activity, 360).apply { orientation = GridLayoutManager.VERTICAL }
        myrv.layoutManager = GridLayoutManager(context, 3)
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
    }


    private fun loadNewManga() {
        GlobalScope.launch {
            try {
                if (source == "MangaFourLife") {
                    Toast.makeText(activity, "Not Available Currently", Toast.LENGTH_SHORT).show()
//                    list = Sources.MANGA_4_LIFE.getManga(pageNumber++).toList()
                } else if (source == "MangaHere") {
                    list = Sources.MANGA_HERE.getManga(pageNumber++).toList()
                } else if (source == "NineAnime") {
                    list = Sources.NINE_ANIME.getManga(pageNumber++).toList()
                }
//                } else if (source == "MangaPark") {
//                    Toast.makeText(activity, "Not Available Currently", Toast.LENGTH_SHORT).show()
////                    list = Sources.MANGA_PARK.getManga(pageNumber++).toList()
//                } else if (source == "MangaMutiny") {
//                    list = Sources.MANGAMUTINY.getManga(pageNumber++).toList()
//                }
//                } else if (source == "Manganelo") {
//                    list = Sources.MANGANELO.getManga(pageNumber++).toList()
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