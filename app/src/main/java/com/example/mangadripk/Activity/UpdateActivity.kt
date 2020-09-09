package com.example.mangadripk.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mangadripk.R
import java.util.*

class UpdateActivity : AppCompatActivity() {
    lateinit var chapterList: MutableList<List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val intent = intent
        var Chapter_List = intent.extras!!.getString("Chapter_List")

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
    }
}