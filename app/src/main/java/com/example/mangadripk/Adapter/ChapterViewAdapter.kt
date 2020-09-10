package com.example.mangadripk.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
//        println(Data[position].read)
        println(Data[position].read)
        if (Data[position].read == "0" ) {
            println("This chapter " + Data[position].name + " should be black" + Data[position].read)
            holder.chapter_title.text = Data[position].name
            holder.chapter_title.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else if (Data[position].read == "1") {
            holder.chapter_title.text = Data[position].name
            holder.chapter_title.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        if (Data[position].new == false) {
            holder.new_icon.visibility = View.INVISIBLE
        } else {
            holder.new_icon.visibility = View.VISIBLE
        }
        holder.cardView.setOnClickListener {
            holder.new_icon.visibility = View.INVISIBLE
            holder.chapter_title.setTextColor(ContextCompat.getColor(context, R.color.gray));

            val model = Data[position]
            val name : String? = model.name
            val url : String? = model.link
            val uploadedtime : Long? = model.uploadedTime
            val source : Sources? = model.sources
            val upload : String? = model.uploaded
            val ogT : String? = model.OG_Name
            val ogN : String? = model.OG_Thumb
            var list_string = ""
            for (i in Data.indices) {
                list_string += Data[i].name + " - " + Data[i]
                    .link + " , "
            }

            val intent = Intent(context, Page_Activity::class.java)
            intent.putExtra("Chapter_List", list_string)
            intent.putExtra("name",name)
            intent.putExtra("url",url)
            intent.putExtra("uploadedtime",uploadedtime)
            intent.putExtra("upload",upload)
            intent.putExtra("source", source.toString())
            intent.putExtra("OGN",ogN)
            intent.putExtra("OGT",ogT)


            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var chapter_title: TextView
        var cardView: CardView
        var new_icon: ImageView

        init {
            chapter_title = itemView.findViewById<View>(R.id.chapter_title) as TextView
            cardView = itemView.findViewById<View>(R.id.chapter) as CardView
            new_icon = itemView.findViewById<View>(R.id.new_icon) as ImageView
        }
    }

    init {
        this.Data = Data
    }
}