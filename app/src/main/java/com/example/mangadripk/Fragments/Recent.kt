package com.example.mangadripk.Fragments

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecentViewAdapter
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Recent : Fragment() {
    private var myAdapter: RecentViewAdapter? = null

    var lstRecent: MutableList<Recent>? = null
    var myDB: FavoriteDB? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_recent, container, false)

        lstRecent = ArrayList<com.example.mangadripk.Classes.Recent>()
        myDB = FavoriteDB(activity)
        val data: Cursor = myDB!!.listContents
        if (data.count == 0) {
            Toast.makeText(activity, "There are no contents in this list!", Toast.LENGTH_LONG).show()
        } else {
            while (data.moveToNext()) {
//                val manga = com.example.mangadripk.Classes.Recent(data.getString(1), "", data.getString(2), data.getString(3), Sources.MANGA_HERE)
//
//                (lstRecent as ArrayList<com.example.mangadripk.Classes.Recent>).add(manga)
////                myDB.close()
                val myrv = view.findViewById(R.id.recent_id) as RecyclerView
                myAdapter = activity?.let { RecentViewAdapter(it, lstRecent as ArrayList<Recent>) }
                myrv.layoutManager = GridLayoutManager(activity, 1)
                myrv.adapter = myAdapter

                (lstRecent as ArrayList<Recent>).add(Recent("Yuh","Chapter 1","https://avt.mkklcdnv6.com/28/k/19-1583500216.jpg","yuh"))
                return view
            }
        }
        return view
    }

}