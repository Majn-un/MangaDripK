package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Activity.UpdateActivity
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Favorite : Fragment() {
    private var myAdapter: RecyclerViewAdapter? = null
    private val progressDialog = CustomProgressDialog()
    private var mangaList = mutableListOf<MangaModel>()
    private var newList = mutableListOf<MangaModel>()
    private lateinit var update_layout: View
    var myDB: FavoriteDB? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)
        update_layout = view.findViewById<View>(R.id.update) as View

        activity?.let { progressDialog.show(it) }
        val toolbar =
            view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        mangaList = ArrayList<MangaModel>()
        try {
            myDB = FavoriteDB(activity)
            val data: Cursor = myDB!!.listContents
            if (data.count == 0) {
                Toast.makeText(activity, "There are no contents in this list!", Toast.LENGTH_LONG)
                    .show()
            } else {
                while (data.moveToNext()) {
                    val manga = MangaModel(
                        data.getString(1), "", data.getString(2), data.getString(
                            3
                        ), Sources.MANGA_HERE
                    )

                    (mangaList as ArrayList<MangaModel>).add(manga)

                }
            }

        } finally {
            myDB!!.close()
        }

        val lstMangaC = mangaList as List<MangaModel>
        val lstMangaRev = lstMangaC.asReversed()

//        println("1"+lstMangaRev)
        GlobalScope.launch {
            val updateList = checkForUpates(lstMangaRev)
            if (updateList?.size != 0) {
                update_layout.visibility = View.VISIBLE
            } else {
                update_layout.visibility = View.GONE
            }

            if (update_layout.visibility == View.VISIBLE) {
                val updateButton = view.findViewById<View>(R.id.update_button) as Button

                updateButton.setOnClickListener {
                    var list_string = ""
                    for (i in updateList!!.indices) {
                        list_string += updateList[i].title + " - " + updateList[i]
                            .imageUrl + " - " + updateList[i].mangaUrl + " , "
                    }
                    val update = Intent(context, UpdateActivity::class.java)
                    update.putExtra("list", list_string)
                    startActivity(update)
                }
            }
//            println("3"+updateList)

        }


        val myrv: RecyclerView = view.findViewById(R.id.favorite_id)
        myAdapter = RecyclerViewAdapter(requireActivity(), lstMangaRev)
        myrv.layoutManager = GridLayoutManager(activity, 3)
        myrv.adapter = myAdapter
        progressDialog.dialog.dismiss()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.search_menu, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = searchViewItem.actionView as SearchView
        searchView.queryHint = "Search..."
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
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

    private fun checkForUpates(list: List<MangaModel>): MutableList<MangaModel>? {
        val light = LinkedList<MangaModel>()
            try {
                for (item in list) {
                    println("item"+item)
                    val doc = Jsoup.connect(item.mangaUrl).cookie("isAdult", "1").get()
                    val chapters = doc.select("div[id=chapterlist]").select("ul.detail-main-list").select(
                        "li"
                    ).eq(0)
                    val newPic = chapters.select("a").select("img.new-pic").isNotEmpty()
                    if (newPic) {
                        light.add(item)
                        println("added to list" + item + " " + light.size)
                    }

                }
                println("2" + light.size)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return light
    }

    private fun parseChapterDate(date: String): Long {
        return if ("Today" in date || " ago" in date) {
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } else if ("Yesterday" in date) {
            Calendar.getInstance().apply {
                add(Calendar.DATE, -1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } else {
            try {
                SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH).parse(date).time
            } catch (e: ParseException) {
                0L
            }
        }
    }
}