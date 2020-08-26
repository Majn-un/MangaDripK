package com.example.mangadripk.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mangadripk.Activity.Manga_Activity
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.Interface.PageImageCallback
import com.example.mangadripk.R
import com.github.chrisbanes.photoview.PhotoView


class WebtoonViewAdapter(
    private val context: Context,
    Data: List<Page>
) :
    RecyclerView.Adapter<WebtoonViewAdapter.MyViewHolder>() {
    private val Data: List<Page>
    private lateinit var pageImageCallback: PageImageCallback

    fun setPageImageCallback(pageImageCallback: PageImageCallback) {
        this.pageImageCallback = pageImageCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.webtoon_pager_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newView: View = LayoutInflater.from(context).inflate(R.layout.webtoon_pager_item, null)
        val web_image: PhotoView = newView.findViewById<View>(R.id.chapterPage) as PhotoView

        Glide.with(context).load(Data[position].link).dontTransform().into(holder.chapter_title)
        holder.chapter_title.setOnClickListener(View.OnClickListener {
            println("yuh")
            pageImageCallback.onClick()
        })


    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var chapter_title: ImageView

//        var cardView: CardView

        init {
            chapter_title = itemView.findViewById<View>(R.id.chapterPage) as ImageView

        }
    }

    init {
        this.Data = Data
    }
}