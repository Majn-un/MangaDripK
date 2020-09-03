package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import java.util.*


class Favorite : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val progressDialog = CustomProgressDialog()

    private var mangaList = mutableListOf<MangaModel>()
    var myDB: FavoriteDB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_favorite, container, false)
        activity?.let { progressDialog.show(it) }
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        mangaList = ArrayList<MangaModel>()
        try {
            myDB = FavoriteDB(activity)
            val data: Cursor = myDB!!.listContents
            if (data.count == 0) {
                Toast.makeText(activity, "There are no contents in this list!", Toast.LENGTH_LONG).show()
            } else {
                while (data.moveToNext()) {
                    val manga = MangaModel(data.getString(1), "", data.getString(2), data.getString(3), Sources.MANGA_HERE)

                    (mangaList as ArrayList<MangaModel>).add(manga)

                }
            }

        } finally {
            myDB!!.close()
        }

        val lstMangaC = mangaList as List<MangaModel>
        val lstMangaRev = lstMangaC.asReversed()
        val myrv: RecyclerView = view.findViewById(R.id.favorite_id)
        myAdapter = RecyclerViewAdapter(activity!!, lstMangaRev)
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


}