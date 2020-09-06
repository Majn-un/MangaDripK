package com.example.mangadripk.Fragments

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageAdapter
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.programmersbox.manga_sources.mangasources.MangaModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Library : Fragment() {
    var viewPager: ViewPager? = null
    var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        val view : View = inflater.inflate(R.layout.fragment_library, container, false)
        val mtoolbar = view.findViewById<Toolbar>(R.id.toolbar)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(mtoolbar)
        }

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tablayout);


        return view
    }


    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager?.let { setUpViewPager(it) }
        tabLayout!!.setupWithViewPager(viewPager)
        tabLayout!!.setSelectedTabIndicatorColor(Color.parseColor("#00B300"));
//        tabLayout!!.setSelectedTabIndicatorHeight((int) (5 * resources.displayMetrics.density));
        tabLayout!!.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#00B300"));
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


}