package com.example.mangadripk.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mangadripk.Activity.Page_Activity
import com.example.mangadripk.Classes.Recent
import com.example.mangadripk.Database.RecentDB
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
        var rmButton: Button
        init {
            chapter_title = itemView.findViewById<View>(R.id.recent_chapter) as TextView
            manga_title = itemView.findViewById<View>(R.id.recent_title) as TextView
            manga_thumb = itemView.findViewById<View>(R.id.recent_thumb) as ImageView
            cardView = itemView.findViewById<View>(R.id.recent) as CardView
            rmButton = itemView.findViewById<View>(R.id.rmbutton) as Button
        }
    }

    init {
        this.Data = Data
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chapter_title.setText(Data[position].chapter)
        holder.manga_title.setText(Data[position].title)
        Glide.with(context).load(Data[position].thumbnail).placeholder(R.drawable.manga_drip_splash_theme).error(R.drawable.manga_drip_splash_theme).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder.manga_thumb)
        holder.rmButton.setOnClickListener(View.OnClickListener {
            var myDB: RecentDB? = null
            myDB = RecentDB(context)
            Data[position].title?.let { it1 -> myDB.deleteData(it1) }
            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT ).show()
        })

        holder.cardView.setOnClickListener {
            val model = Data[position]

            val name : String? = model.chapter
            val url : String? = model.Link
//            val uploadedtime : Long? = model.uploadedTime
            val source : Sources? = Sources.MANGA_HERE
//            val upload : String? = model.uploaded
            val ogT : String? = model.title
            val ogN : String? = model.thumbnail
            val chapter : String? = model.chapter_link
            val intent = Intent(context, Page_Activity::class.java)
            intent.putExtra("Chapter_List", chapter)
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