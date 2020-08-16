package com.example.mangadripk.Activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangadripk.Adapter.RecyclerViewAdapter
import com.example.mangadripk.Classes.CustomProgressDialog
import com.example.mangadripk.Database.FavoriteDB
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Manga_Activity : AppCompatActivity() {
    private val myAdapter: RecyclerViewAdapter? = null
    private var button_for_chapters: Button? = null
    private var button_for_favorites: Button? = null
    private var button_for_resume: Button? = null
    private val progressDialog = CustomProgressDialog()
    var myDB: FavoriteDB? = null
    private var manga_title: TextView? = null
    private var manga_description: TextView? = null
    private var manga_status: TextView? = null
    private var manga_author: TextView? = null
    private var img: ImageView? = null
    private var Manga_URL: String? = null
    private var ShineManga: MangaModel = MangaModel("","","","",Sources.MANGA_HERE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga)
        progressDialog.show(this)

        manga_status = findViewById<View>(R.id.status) as TextView
        manga_author = findViewById<View>(R.id.author) as TextView
        manga_title = findViewById<View>(R.id.depth_title) as TextView
        manga_description = findViewById<View>(R.id.depth_description) as TextView
        img = findViewById<View>(R.id.manga_thumbnail) as ImageView
        val intent = intent
        Manga_URL = intent.getStringExtra("mangaUrl")
        val imgUrl = intent.getStringExtra("imgUrl")
        val description = intent.getStringExtra("description")
        val title = intent.getStringExtra("title")


        val MangaYUH = title?.let {
            if (description != null) {
                if (Manga_URL != null) {
                    if (imgUrl != null) {
                        ShineManga = MangaModel(it,description, Manga_URL!!,imgUrl,Sources.MANGA_HERE)
                    }
                }
            }
        }

        MangaActivity()

        button_for_chapters = findViewById<View>(R.id.chapters_button) as Button
        button_for_chapters!!.setOnClickListener {

            val intent = Intent(this@Manga_Activity, Chapter_Activity::class.java)
            intent.putExtra("mangaUrl",Manga_URL)
            intent.putExtra("imgUrl",imgUrl)
            intent.putExtra("description",description)
            intent.putExtra("title",title)
            intent.putExtra("source",Sources.toString())
            startActivity(intent)
        }

        button_for_resume = findViewById<View>(R.id.resume) as Button
        button_for_resume!!.setOnClickListener {
            TODO("IMPLEMENT RESUME ALGORITHM")
        }

        myDB = FavoriteDB(this)
        button_for_favorites = findViewById<View>(R.id.favorite_button) as Button
        button_for_favorites!!.setOnClickListener {
            updateFavorite()
        }

    }

    private fun updateFavorite() {
        myDB = FavoriteDB(this)
        val data: Cursor = myDB!!.listContents
        while (data.moveToNext()) {
            if (data.getString(2) == Manga_URL) {
                Toast.makeText(this, "Already Favorited", Toast.LENGTH_SHORT).show()
                return
                TODO("REPLACES THE ITEM WITH THE SAME NAME")
            }
        }
        myDB!!.addData(ShineManga)
        Toast.makeText(this, "Added to Favorites", Toast.LENGTH_SHORT).show()
        myDB!!.close()
    }

    private fun AddData(
        manga : MangaModel
    ) {
        myDB = FavoriteDB(this)
        val insertData = myDB!!.addData(manga)

        if (insertData == true) {
            Toast.makeText(this@Manga_Activity, "Successfully Entered Data!", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this@Manga_Activity, "Yuh it no work", Toast.LENGTH_LONG).show()
        }
    }

    private fun MangaActivity() {
        GlobalScope.launch {
            try {
                val mangaActivity = ShineManga.toInfoModel()
                setValues(mangaActivity.description,mangaActivity.author,mangaActivity.status,ShineManga.title,ShineManga.imageUrl)
                println(mangaActivity.status)
                runOnUiThread {
                    myAdapter?.notifyDataSetChanged()
                }
                progressDialog.dialog.dismiss()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setValues(
        description: String,
        author: String,
        status: String,
        title: String,
        img_URL: String
    ) {
        runOnUiThread {
            manga_description!!.text = description
            manga_author!!.text = author
            manga_status!!.text = "Status: $status"
            manga_title!!.text = title
            Picasso.get().load(img_URL).into(img)

        }
    }
}