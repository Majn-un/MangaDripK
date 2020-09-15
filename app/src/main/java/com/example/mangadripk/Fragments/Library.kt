package com.example.mangadripk.Fragments

import android.app.AlertDialog
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageAdapter
import com.example.mangadripk.Database.Source
import com.example.mangadripk.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class Library : Fragment() {
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null
    private var source: Button? = null
    var myDB: Source? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        val view : View = inflater.inflate(R.layout.fragment_library, container, false)

        source = view.findViewById<View>(R.id.sources) as Button

        myDB = Source(activity)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            source!!.text = data.getString(1)
        }
        if (source!!.text == "Source") {
            myDB!!.addData("MangaHere")
            source!!.text = "MangaHere"
        }

        myDB!!.close()
        source!!.setOnClickListener {
            showDialog()
        }

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        viewPager = view.findViewById(R.id.pager)
        tabLayout = view.findViewById(R.id.tablayout)

        return view
    }


    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager?.let { setUpViewPager(it) }
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setSelectedTabIndicatorColor(Color.parseColor("#00B300"))
        tabLayout!!.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#00B300"))
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        val adapter = PageAdapter(childFragmentManager)
        adapter.addFragment(FragAll(), "All")
        adapter.addFragment(FragLatest(), "Latest")
        adapter.addFragment(FragRanked(), "Ranked")
        viewPager.adapter = adapter
    }

    private fun showDialog() {
        lateinit var dialog: AlertDialog
        lateinit var choice: String

        val listItems = arrayOf("MangaHere", "MangaFourLife", "NineAnime")
        val builder = AlertDialog.Builder(activity)
        val checkInt = listItems.indexOf(source!!.text)
        builder.setTitle("Select Source")
        builder.setSingleChoiceItems(listItems, checkInt) { _, i ->
            choice = listItems[i]
        }

        builder.setNeutralButton("Cancel") { _, i ->
            dialog.cancel()
        }
        builder.setPositiveButton("OK") { _, _ ->
            source!!.text = choice

            myDB = Source(activity)
            myDB!!.clearDatabase()
            myDB!!.addData(choice)
            myDB!!.close()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, Library())?.commit()
        }
        dialog = builder.create()
        dialog.show()
    }
}
