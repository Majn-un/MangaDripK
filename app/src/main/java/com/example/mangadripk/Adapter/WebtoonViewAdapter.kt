package com.example.mangadripk.Adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
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
//        val display: DisplayMetrics = context.resources.displayMetrics
//        var height: Int = 0
//        val params = RecyclerView.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        holder.chapter_title.layoutParams = params
        holder.chapter_title.scaleType = ImageView.ScaleType.FIT_CENTER
        holder.chapter_title.adjustViewBounds = true
//        Thread {
//            try {
//                val url_call : URL = URL(Data[position].link)
//                val bmp = BitmapFactory.decodeStream(url_call.openConnection().getInputStream())
//                height = bmp.height
//            } catch (ignored: IOException) {
//                Log.d("Yuh", "Something is not working")
//            }
//        }.start()
//        println(height)
//        val newHeight = 4000
//        val newWidth = display.widthPixels
//        holder.chapter_title.requestLayout()
//        holder.chapter_title.getLayoutParams().height = newHeight
//        holder.chapter_title.layoutParams.width = newWidth
//

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