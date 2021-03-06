package com.example.mangadripk.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Adapter.WebtoonViewAdapter
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.ReadDb
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.Dialogs.CopyrightDialog
import com.example.mangadripk.Interface.PageImageCallback
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.ChapterModel
import kotlinx.android.synthetic.main.activity_viewer.presenterVertical
import kotlinx.android.synthetic.main.activity_viewer.presenter1Vertical
import kotlinx.android.synthetic.main.activity_webtoon.presenterHorizontal
import kotlinx.android.synthetic.main.activity_webtoon.presenter1Horizontal


import kotlinx.android.synthetic.main.activity_viewer.*
import kotlinx.android.synthetic.main.activity_webtoon.*
//import kotlinx.android.synthetic.main.presenter
//import kotlinx.android.synthetic.main.presenter1


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class Page_Activity : AppCompatActivity(),
    PageImageCallback, PopupMenu.OnMenuItemClickListener {
    lateinit var lstPages: MutableList<Page>
    lateinit var chapterList: MutableList<List<String>>
    private var myViewPager: PageViewAdapter? = null
    private var Webtoon: WebtoonViewAdapter? = null
    private var reading_direction: Float? = 180F
    private lateinit var myrv: ViewPager
    private var chapter: TextView? = null
//    private var presenter1Horizontal: Toolbar? = null
//    private var presenterHorizontal: Toolbar? = null
//    private var presenterVertical: Toolbar? = null
//    private var presenter1Vertical: Toolbar? = null

    private var title: TextView? = null
    private var view: Int = 1
    private var Page_Model: ChapterModel = ChapterModel("", "", "", Sources.MANGA_HERE, false)
    var myDB: RecentDB? = null
    var myReadDB: ReadDb? = null
    var OG_name : String? = ""
    var name : String? = ""
    var recent : Recent = Recent("", "", "", "", "")
    private val progressDialog = CustomProgressDialog()
    var Chapter_List: String? = ""
    private var index = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        progressDialog.show(this)




        lstPages = ArrayList()

        getPrevData()
        updateDB()
        updateRead()
        mangaPages()
        MangaView()



    }

    private fun MangaView() {
        setContentView(R.layout.activity_viewer)
        val next = findViewById<View>(R.id.next) as Button
        val back = findViewById<View>(R.id.back) as Button
        val toChapters = findViewById<View>(R.id.to_chapters) as Button
        toChapters.setOnClickListener(View.OnClickListener {
            if (!NavController(this).popBackStack()) {
                finish()
            }

        })
        title = findViewById<View>(R.id.manga_name) as TextView
        chapter = findViewById<View>(R.id.chapter_name) as TextView
        title!!.text = OG_name
        chapter!!.text = name

        val myrv = findViewById<View>(R.id.right_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myrv.rotationY = reading_direction!!

        myrv.setPageTransformer(false,
            ViewPager.PageTransformer { page, position ->
                page.rotationY =
                    reading_direction as Float
            })
        myViewPager!!.setPageImageCallback(this)

        myrv.adapter = myViewPager

        next.setOnClickListener(View.OnClickListener {
            if (index == 0) {
                Log.d("Newest Chapter Enabled", "YUH")
            } else {
                Page_Model = ChapterModel(
                    chapterList[index - 1][0] as String,
                    chapterList[index - 1][1] as String,
                    "",
                    Sources.MANGA_HERE,
                    false
                )
                index -= 1
                lstPages = ArrayList()
                mangaPages()
                chapter!!.text = chapterList[index][0]
                myViewPager = PageViewAdapter(this@Page_Activity, lstPages)
                myViewPager!!.setPageImageCallback(this)
                myrv.adapter = myViewPager
            }
        })

        back.setOnClickListener(View.OnClickListener {
            if (index + 1 == chapterList.size) {
                Log.d("Oldest Chapter Enabled", "YUH")
            } else {

                Page_Model = ChapterModel(
                    chapterList[index + 1][0],
                    chapterList[index + 1][1],
                    "",
                    Sources.MANGA_HERE,
                    false
                )
                index += 1
                lstPages = ArrayList()
                mangaPages()
                chapter!!.text = chapterList[index][0]
                myViewPager = PageViewAdapter(this@Page_Activity, lstPages)
                myViewPager!!.setPageImageCallback(this)
                myrv.adapter = myViewPager

            }
        })
    }

    private fun WebToonView() {
        setContentView(R.layout.activity_webtoon)
        val next = findViewById<View>(R.id.next) as Button
        val back = findViewById<View>(R.id.back) as Button
        title = findViewById<View>(R.id.manga_name) as TextView
        chapter = findViewById<View>(R.id.chapter_name) as TextView
        title!!.text = OG_name
        chapter!!.text = name
        val myrv = findViewById<View>(R.id.recycler) as RecyclerView
        Webtoon = WebtoonViewAdapter(this, lstPages)
        Webtoon!!.setPageImageCallback(this)
        myrv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        myrv.adapter = Webtoon

    }


    private fun getPrevData() {
        title = findViewById<View>(R.id.manga_name) as TextView
        chapter = findViewById<View>(R.id.chapter_name) as TextView
        val intent = intent
        val url = intent.getStringExtra("url")
        OG_name = intent.getStringExtra("OGT")
        val uploadedTime = intent.getStringExtra("uploadtime")
        val OG_thumb = intent.getStringExtra("OGN")
        name = intent.getStringExtra("name")
        val upload = intent.getStringExtra("upload")
        Chapter_List = intent.extras!!.getString("Chapter_List")
        val aList: ArrayList<*> = ArrayList<Any?>(
            Arrays.asList(
                *Chapter_List!!.split(",".toRegex()).toTypedArray()
            )
        )
        chapterList = ArrayList()
        for (i in aList.indices) {
            val beta = aList[i].toString().split("-")
            chapterList.add(beta)
        }

        for (i in chapterList.indices) {
            if (chapterList[i][0] == " $name ") {
                index = i
                break
            }
        }

        val MangaYUH = name?.let {
            if (url != null) {
                if (upload != null) {
                    Page_Model = ChapterModel(it, url, upload, Sources.MANGA_HERE,false)

                }
            }
        }

        if (OG_name!!.contains("\'")) {
            val name_without = OG_name!!.replace("\'", "")
            recent = Recent(name_without, name, OG_thumb, Page_Model.url, Chapter_List)
        } else {
            recent = Recent(OG_name, name, OG_thumb, Page_Model.url, Chapter_List)
        }



    }

    private fun updateDB() {
        try {
            myDB = RecentDB(this)
            val data: Cursor = myDB!!.listContents
            while (data.moveToNext()) {
                if (data.getString(3) == OG_name) {
                    myDB!!.deleteData(data.getString(3))
                }
            }
            myDB!!.addData(recent)
        } finally {
            myDB!!.close()

        }
    }

    private fun updateRead() {
        try {
            myReadDB = ReadDb(this)
            val read: Cursor = myReadDB!!.listContents
            while (read.moveToNext()) {
                if (read.getString(1) == OG_name) {
                    if (read.getString(3) == name) {
                        myReadDB!!.deleteData(read.getString(1))
                    }
                }

            }
            val chapter = Chapter(name, "", Sources.MANGA_HERE, "", null, OG_name, "", "0",false)
            myReadDB!!.addData(chapter)
        } finally {
            myReadDB!!.close()
        }
    }

    private fun mangaPages() {
        GlobalScope.launch {
            try {

                val mangaActivity = Page_Model.getPageInfo()
                if (mangaActivity.pages.isEmpty()) {
                    openDialog()
                }

                for (i in mangaActivity.pages.indices) {
                    val page = Page(mangaActivity.pages[i], (i + 1).toString())
                    println(page.link)
                    lstPages.add(page)
                }

                runOnUiThread {
                    myViewPager?.notifyDataSetChanged()
                }

                progressDialog.dialog.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun openDialog() {
        val exampleDialog = CopyrightDialog()
        exampleDialog.show(supportFragmentManager, "example dialog")
    }

    override fun onClick() {
        val presenterHorizontal11 = findViewById<Toolbar>(R.id.presenterHorizontal)
        val presenter11Horizontal = findViewById<Toolbar>(R.id.presenter1Horizontal)
        val presenterVertical22 = findViewById<Toolbar>(R.id.presenterVertical)
        val presenter11Vertical = findViewById<Toolbar>(R.id.presenter1Vertical)

        if (view == 1) {
            if (presenterVertical22.visibility == View.INVISIBLE) {
                presenterVertical22.visibility = View.VISIBLE
                presenter11Vertical.visibility = View.VISIBLE
            } else {
                presenterVertical22.visibility = View.INVISIBLE
                presenter11Vertical.visibility = View.INVISIBLE
            }
        }

        if (view == 2) {
            if (presenterHorizontal11.visibility == View.INVISIBLE) {
                println("webtoon visible")
                presenterHorizontal11.visibility = View.VISIBLE
                presenter11Horizontal.visibility = View.VISIBLE
            } else {
                presenterHorizontal11.visibility = View.INVISIBLE
                presenter11Horizontal.visibility = View.INVISIBLE
            }
        }

    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return when (p0?.itemId) {
            R.id.item1 -> {
                view = 1
                MangaView()
                true
            }
            R.id.item2 -> {
                view = 2
                WebToonView()
                true
            }
            R.id.item3 -> {
                true
            }
            R.id.left -> {
                view = 1
                reading_direction = 0F
                MangaView()

                true
            }
            R.id.right -> {
                view = 1
                reading_direction = 180F
                MangaView()
                true
            }

            else -> false
        }
    }

    fun showPopup(v: View?) {
        val popup = PopupMenu(this, v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }
}