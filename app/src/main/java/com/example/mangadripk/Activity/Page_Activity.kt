package com.example.mangadripk.Activity

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Adapter.WebtoonViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.RecentDB
import com.example.mangadripk.Interface.PageImageCallback
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.ChapterModel
import com.programmersbox.manga_sources.mangasources.PageModel
import kotlinx.android.synthetic.main.activity_viewer.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*


class Page_Activity : AppCompatActivity(),
    PageImageCallback, PopupMenu.OnMenuItemClickListener {
    lateinit var lstPages: MutableList<Page>
    lateinit var chapterList: MutableList<List<String>>
    private var myViewPager: PageViewAdapter? = null
    private var Webtoon: WebtoonViewAdapter? = null

    private var Page_Model: ChapterModel = ChapterModel("","","",Sources.MANGA_HERE)
    var myDB: RecentDB? = null
    var OG_name : String? = ""
    var name : String? = ""

    var recent : Recent = Recent("","","","","")
    private val progressDialog = CustomProgressDialog()
    var Chapter_List: String? = ""
    private var index = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
        progressDialog.show(this)
        val next = findViewById<View>(R.id.next) as Button
        val back = findViewById<View>(R.id.back) as Button
        val title = findViewById<View>(R.id.manga_name) as TextView
        val chapter = findViewById<View>(R.id.chapter_name) as TextView
//

        val frame = findViewById<FrameLayout>(R.id.manga_reader) as FrameLayout
        val presenter = findViewById<View>(R.id.presenter) as Toolbar
        val presenter1 = findViewById<View>(R.id.presenter1) as Toolbar

        val intent = intent
        val url = intent.getStringExtra("url")
        OG_name = intent.getStringExtra("OGT")
        title.text = OG_name
        val OG_thumb = intent.getStringExtra("OGN")
        name = intent.getStringExtra("name")
        chapter.text = name
        val upload = intent.getStringExtra("upload")
        Chapter_List = intent.extras!!.getString("Chapter_List")
        val aList: ArrayList<*> = ArrayList<Any?>(
            Arrays.asList(*Chapter_List!!.split(",".toRegex()).toTypedArray()
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



        val uploadedTime = intent.getStringExtra("uploadtime")
        val MangaYUH = name?.let {
            if (url != null) {
                if (upload != null) {
                        Page_Model = ChapterModel(it, url,upload, Sources.MANGA_HERE)

                }
            }
        }

        recent = Recent(OG_name,name,OG_thumb,Page_Model.url,Chapter_List)
        updateRecent()

        lstPages = ArrayList()
        mangaPages()





        val myrv = findViewById<View>(R.id.view_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myViewPager!!.setPageImageCallback(this)
        myrv.adapter = myViewPager
        println(lstPages)

        next.setOnClickListener(View.OnClickListener {
            println(chapterList[0])

            if (index == 0) {
                Log.d("Newest Chapter Enabled", "YUH")
            } else {
                Page_Model = ChapterModel(chapterList[index-1][0] as String, chapterList[index - 1][1] as String,"",Sources.MANGA_HERE)
                //                    Log.d("Chapter Link Previous",Chapter_URL );
                index = index - 1
                lstPages = ArrayList()
                mangaPages()
                chapter.text = chapterList[index][0]
                myViewPager = PageViewAdapter(this@Page_Activity, lstPages)
                myViewPager!!.setPageImageCallback(this)
                myrv.adapter = myViewPager
            }
        })
//

//        frame.setOnClickListener(View.OnClickListener {
//            println("Clicked")
//            if (presenter.visibility == View.INVISIBLE) {
//                presenter.visibility = View.VISIBLE
//                presenter1.visibility = View.VISIBLE
//            } else {
//                presenter.visibility = View.INVISIBLE
//                presenter1.visibility = View.INVISIBLE
//
//
//            }
//        })

//
        back.setOnClickListener(View.OnClickListener {
            if (index + 1 == chapterList.size) {
                Log.d("Oldest Chapter Enabled", "YUH")
            } else {

                Page_Model = ChapterModel(chapterList[index + 1][0], chapterList[index + 1][1],"",Sources.MANGA_HERE)
                index = index + 1
                //                    Log.d("Chapter Link Next", Chapter_URL);
                lstPages = ArrayList()
                mangaPages()
                chapter.text = chapterList[index][0]
                myViewPager = PageViewAdapter(this@Page_Activity, lstPages)
                myViewPager!!.setPageImageCallback(this)
                myrv.adapter = myViewPager

            }
        })

    }



    private fun updateRecent() {
        myDB = RecentDB(this)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            if (data.getString(3) == OG_name) {
                println("Already in recents")
                myDB!!.deleteData(data.getString(3))
            }
        }
        myDB!!.addData(recent)
        myDB!!.close()

    }

    private fun AddData(
        manga : Recent
    ) {
        myDB = RecentDB(this)
        val insertData = myDB!!.addData(manga)

        if (insertData == true) {
            Toast.makeText(this@Page_Activity, "Successfully Entered Data!", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this@Page_Activity, "Yuh it no work", Toast.LENGTH_LONG).show()
        }
    }

    private fun mangaPages() {
        GlobalScope.launch {
            try {

                val mangaActivity = Page_Model.getPageInfo()
                for (i in 0 until mangaActivity.pages.size) {
                    val page = Page(mangaActivity.pages[i],(i+1).toString())
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

    override fun onClick() {
        if (presenter.visibility == View.INVISIBLE) {
            presenter.visibility = View.VISIBLE
            presenter1.visibility = View.VISIBLE
        } else {
            presenter.visibility = View.INVISIBLE
            presenter1.visibility = View.INVISIBLE
        }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return when (p0?.itemId) {
            R.id.item1 -> {
                    val myrv = findViewById<View>(R.id.view_page) as ViewPager
                    myViewPager = PageViewAdapter(this, lstPages)
                    myViewPager!!.setPageImageCallback(this)
                    myrv.adapter = myViewPager
                    true

            }
            R.id.item2 -> {
                setContentView(R.layout.activity_webtoon)
                    val myrv = findViewById<View>(R.id.recycler) as RecyclerView
                    Webtoon = WebtoonViewAdapter(this, lstPages)
                    myrv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//                    myViewPager!!.setPageImageCallback(this)
                    myrv.adapter = Webtoon
                    true
            }
            R.id.item3 -> {
                Toast.makeText(this, "Item 3 clicked", Toast.LENGTH_SHORT).show()
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