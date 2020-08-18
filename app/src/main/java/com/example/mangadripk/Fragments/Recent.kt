package com.example.mangadripk.Fragments

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecentViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.R
import java.util.*


class Recent : Fragment() {
    private var myAdapter: RecentViewAdapter? = null
    private val progressDialog = CustomProgressDialog()

    private var lstRecent: MutableList<Recent>? = null
    var myDB: RecentDB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val view: View = inflater.inflate(R.layout.fragment_recent, container, false)

        activity?.let { progressDialog.show(it) }

        lstRecent = ArrayList<Recent>()
        myDB = RecentDB(activity)

        val data: Cursor = myDB!!.listContents
        if (data.count == 0) {
            Toast.makeText(activity, "There are no contents in this list!", Toast.LENGTH_SHORT)
                .show()
        } else {
            while (data.moveToNext()) {
                val manga = Recent(
                    data.getString(3),
                    data.getString(1),
                    data.getString(2),
                    data.getString(4)
                )
                (lstRecent as ArrayList<Recent>).add(manga)
            }
        }

        myDB!!.close()
        val lstRev = lstRecent as List<Recent>
        val lstRev2 = lstRev.asReversed()
        val myrv = view.findViewById(R.id.recent_id) as RecyclerView
        myAdapter = activity?.let { RecentViewAdapter(it, lstRev2) }
        myrv.layoutManager = GridLayoutManager(activity, 1)
        myrv.adapter = myAdapter
        progressDialog.dialog.dismiss()

        return view
    }

    /// FUCKS UP THE RESVERSAL


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
//                    var newText = s
//                    newText = newText.toLowerCase()
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
//                    var newText = newText
//                    newText = newText.toLowerCase()
//                    val newList: ArrayList<Recent> = ArrayList<Recent>()
//                    if (lstRecent != null) {
//                        for (manga in lstRecent!!) {
//                            val title: String? = manga.title?.toLowerCase()
//                            if (title != null) {
//                                if (title.contains(newText)) {
//                                    newList.add(manga)
//                                }
//                            }
//                        }
//                    }
//                    myAdapter?.setFilter(newList)
//                    return false
//                }
//            }
//        searchView.setOnQueryTextListener(queryTextListener)
//    }

}