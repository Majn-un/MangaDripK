package com.example.mangadripk.Fragments

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    var lstManga: List<MangaModel>? = null
    var myDB: FavoriteDB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_favorite, container, false)
        activity?.let { progressDialog.show(it) }

        lstManga = ArrayList<MangaModel>()
        myDB = FavoriteDB(activity)
        val data: Cursor = myDB!!.listContents
        if (data.count == 0) {
            Toast.makeText(activity, "There are no contents in this list!", Toast.LENGTH_LONG).show()
        } else {
            while (data.moveToNext()) {
                val manga = MangaModel(data.getString(1), "", data.getString(2), data.getString(3), Sources.MANGA_HERE)

                (lstManga as ArrayList<MangaModel>).add(manga)

            }
        }
        val lstMangaC = lstManga as List<MangaModel>
        val lstMangaRev = lstMangaC.asReversed()
        myDB!!.close()
        val myrv: RecyclerView = view.findViewById(R.id.favorite_id)
        myAdapter = RecyclerViewAdapter(activity!!, lstMangaRev)
        myrv.layoutManager = GridLayoutManager(activity, 3)
        myrv.adapter = myAdapter
        progressDialog.dialog.dismiss()
        return view
    }


}