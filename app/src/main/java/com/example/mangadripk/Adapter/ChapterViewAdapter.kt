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
            val intent = Intent(context, Page_Activity::class.java)
            intent.putExtra("Name", Data[position].name)
            intent.putExtra("Link", Data[position].link)
            intent.putExtra("ci_session", Data[position].cookie1)
            intent.putExtra("__cfduid", Data[position].cookie2)
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