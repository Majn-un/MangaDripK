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
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Download : Fragment() {
    private val progressDialog = CustomProgressDialog()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_download, container, false)
        activity?.let { progressDialog.show(it) }
        progressDialog.dialog.dismiss()
        return view
    }


}