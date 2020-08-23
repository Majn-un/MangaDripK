package com.example.mangadripk.Adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.R

class WebtoonViewAdapter(
    private val context: Context,
    Data: List<Page>
) :
    RecyclerView.Adapter<WebtoonViewAdapter.MyViewHolder>() {
    private val Data: List<Page>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.webtoon_pager_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        val height = displayMetrics.heightPixels
//        val width = displayMetrics.
//        holder.chapter_title.maxHeight = 199
//        println(holder.chapter_title.height)
//        println(holder.chapter_title.width)
//        println("turn me up")

//        val params: ViewGroup.LayoutParams = holder.chapter_title.getLayoutParams() as ViewGroup.LayoutParams
//        params.width = 1400
////        params.height = 1700
//
//        holder.chapter_title.setLayoutParams(params);
        Glide.with(context).load(Data[position].link).into(holder.chapter_title);
//        Picasso.get().load(Data[position].link).fit().centerInside().into(holder.chapter_title)

//        holder.cardView.setOnClickListener {
//            println("turn me up")
//        }
    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var chapter_title: ImageView
//        var cardView: CardView

        init {
            chapter_title = itemView.findViewById<View>(R.id.chapterPage) as ImageView

//            cardView = itemView.findViewById<View>(R.id.pages) as CardView
        }
    }

    init {
        this.Data = Data
    }
}