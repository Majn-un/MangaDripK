package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.android.synthetic.main.activity_manga.*
import kotlinx.android.synthetic.main.fragment_library.*
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


    private fun loadNewManga() {
        GlobalScope.launch {
            try {
                val list = Sources.MANGA_HERE.getManga(1).toList()
                mangaList.addAll(list)
                Objects.requireNonNull(activity)?.runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}