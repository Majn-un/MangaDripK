package com.example.mangadripk.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mangadripk.Activity.Page_Activity
import com.example.mangadripk.Classes.Chapter
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources

class ChapterViewAdapter(
    private val context: Context,
    Data: List<Chapter>
) :
    RecyclerView.Adapter<ChapterViewAdapter.MyViewHolder>() {
    private val Data: List<Chapter>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.item_chapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chapter_title.setText(Data[position].name)
        holder.cardView.setOnClickListener {
            val model = Data[position]
            val name : String? = model.name
            val url : String? = model.link
            val uploadedtime : Long? = model.uploadedTime
            val source : Sources? = model.sources
            val upload : String? = model.uploaded

            val intent = Intent(context, Page_Activity::class.java)
            intent.putExtra("name",name)
            intent.putExtra("url",url)
            intent.putExtra("uploadedtime",uploadedtime)
            intent.putExtra("upload",upload)
            intent.putExtra("source", source.toString())

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var chapter_title: TextView
        var cardView: CardView

        init {
            chapter_title = itemView.findViewById<View>(R.id.chapter_title) as TextView
            cardView = itemView.findViewById<View>(R.id.chapter) as CardView
        }
    }

    init {
        this.Data = Data
    }
}