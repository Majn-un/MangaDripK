package com.example.mangadripk.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.programmersbox.mangaworld.views.Utility
import java.util.*

class UpdateActivity : AppCompatActivity() {
    private var myAdapter: RecyclerViewAdapter? = null
    lateinit var gridlayoutManager: GridLayoutManager
    private val chapterList = mutableListOf<MangaModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val intent = intent
        val Chapter_List = intent.extras!!.getString("list")

        val aList: ArrayList<*> = ArrayList<Any?>(
            Arrays.asList(
                *Chapter_List!!.split(",".toRegex()).toTypedArray()
            )
        )
        for (i in aList.indices) {
            val beta = aList[i].toString().split("-")
            if (beta.size != 1) {
                val image = beta[1].replace("\\s+".toRegex(), "")
                val alpha = MangaModel(beta[0],"",beta[1],image, Sources.MANGA_HERE)
                chapterList.add(alpha)
            }
        }


        val myrv = findViewById<RecyclerView>(R.id.update_id)
        myAdapter = RecyclerViewAdapter(this, chapterList)
        gridlayoutManager = Utility(this, 400).apply { orientation = GridLayoutManager.VERTICAL }
        myrv.layoutManager = gridlayoutManager
        myrv.adapter = myAdapter

    }
}