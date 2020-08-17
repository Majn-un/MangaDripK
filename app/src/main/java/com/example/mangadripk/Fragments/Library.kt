package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Manga
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Library : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val mangaList = mutableListOf<MangaModel>()
    private val progressDialog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view : View = inflater.inflate(R.layout.fragment_library, container, false)
        activity?.let { progressDialog.show(it) }
        loadNewManga()

        val myrv = view.findViewById(R.id.recyclerview_id) as RecyclerView
        myAdapter = activity?.let { RecyclerViewAdapter(it, mangaList) }
        myrv.layoutManager = GridLayoutManager(activity, 3)
        myrv.adapter = myAdapter
        progressDialog.dialog.dismiss()

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
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    var newText = newText
                    newText = newText.toLowerCase()
                    val obj = Sources.MANGA_HERE.searchManga(newText, 1, mangaList)
                    println(obj)
                    val newList: ArrayList<MangaModel> = ArrayList<MangaModel>()
                    for (manga in mangaList) {
                        val title: String = manga.title.toLowerCase()
                        if (title.contains(newText)) {
                            newList.add(manga)
                        }
                    }
                    myAdapter?.setFilter(newList)
                    return false
                }
            }
        searchView.setOnQueryTextListener(queryTextListener)
    }

    private fun loadNewManga() {

        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(1).toList()
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