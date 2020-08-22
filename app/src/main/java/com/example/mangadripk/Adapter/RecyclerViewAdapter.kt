package com.example.mangadripk.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mangadripk.Activity.Manga_Activity
import com.example.mangadripk.R
import com.programmersbox.manga_sources.mangasources.MangaModel
import com.squareup.picasso.Picasso


class RecyclerViewAdapter (
    private val context: Context,
    private var Data: List<MangaModel>
) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.item_manga, parent, false)
        return MyViewHolder(
            view
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.manga_title.text = Data[position].title
        Picasso.get().load(Data[position].imageUrl).into(holder.manga_img)
        holder.cardView.setOnClickListener(View.OnClickListener {
            val model = Data[position]
            val mangaUrl : String = model.mangaUrl
            val imgUrl : String = model.imageUrl
            val description : String = model.description
            val title : String = model.title

            val intent = Intent(context, Manga_Activity::class.java)
                intent.putExtra("mangaUrl",mangaUrl)
                intent.putExtra("imgUrl",imgUrl)
                intent.putExtra("description",description)
                intent.putExtra("title",title)

            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return Data.size
    }

    class MyViewHolder(itemView: View) : ViewHolder(itemView) {
        var manga_title: TextView
        var manga_img: ImageView
        var cardView: CardView

        init {
            manga_title = itemView.findViewById<View>(R.id.manga_title_id) as TextView
            manga_img = itemView.findViewById<View>(R.id.manga_cover_id) as ImageView
            cardView = itemView.findViewById<View>(R.id.manga) as CardView
        }
    }

    fun setFilter(newList: ArrayList<MangaModel>) {
        Data = ArrayList()
        (Data as ArrayList<MangaModel>).addAll(newList)
        notifyDataSetChanged()
    }


}