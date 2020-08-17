package com.example.mangadripk.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mangadripk.Activity.Page_Activity
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.R
import com.example.mangadripk.Sources.Sources
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class RecentViewAdapter(
    private val context: Context,
    Data: List<Recent>
) :
    RecyclerView.Adapter<RecentViewAdapter.MyViewHolder>() {
    private var Data: List<Recent>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.item_recent, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var chapter_title: TextView
        var manga_title: TextView
        var manga_thumb: ImageView
        var cardView: CardView

        init {
            chapter_title = itemView.findViewById<View>(R.id.recent_chapter) as TextView
            manga_title = itemView.findViewById<View>(R.id.recent_title) as TextView
            manga_thumb = itemView.findViewById<View>(R.id.recent_thumb) as ImageView
            cardView = itemView.findViewById<View>(R.id.recent) as CardView
        }
    }

    init {
        this.Data = Data
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chapter_title.setText(Data[position].chapter)
        holder.manga_title.setText(Data[position].title)
        Picasso.get().load(Data[position].thumbnail).into(holder.manga_thumb)

        holder.cardView.setOnClickListener {
            val model = Data[position]
            val name : String? = model.chapter
            val url : String? = model.Link
//            val uploadedtime : Long? = model.uploadedTime
            val source : Sources? = Sources.MANGA_HERE
//            val upload : String? = model.uploaded
            val ogT : String? = model.thumbnail
            val ogN : String? = model.title

            val intent = Intent(context, Page_Activity::class.java)
            intent.putExtra("Chapter_List", "list_string")
            intent.putExtra("name",name)
            intent.putExtra("url",url)
            intent.putExtra("uploadedtime","uploadedtime")
            intent.putExtra("upload","upload")
            intent.putExtra("source", source.toString())
            intent.putExtra("OGN",ogN)
            intent.putExtra("OGT",ogT)


            context.startActivity(intent)
        }
    }

    fun setFilter(newList: ArrayList<Recent>) {
        Data = ArrayList()
        (Data as ArrayList<Recent>).addAll(newList)
        notifyDataSetChanged()
    }

}