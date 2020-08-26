package com.example.mangadripk.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.mangadripk.Interface.PageImageCallback
import com.example.mangadripk.Classes.Page
import com.example.mangadripk.R
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso


class PageViewAdapter(
    var context: Context,
    PageList: List<Page>
) :
    PagerAdapter() {

    var PageList: List<Page>
    var inflater: LayoutInflater
    private lateinit var pageImageCallback: PageImageCallback

    fun setPageImageCallback(pageImageCallback: PageImageCallback) {
        this.pageImageCallback = pageImageCallback
    }
    override fun getCount(): Int {
        return PageList.size
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view == `object`
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image_layout: View = inflater.inflate(R.layout.view_pager_item, container, false)
        val page_image: PhotoView = image_layout.findViewById<View>(R.id.page_image) as PhotoView

        Glide.with(image_layout).load(PageList[position].link).into(page_image)


        page_image.setOnClickListener(View.OnClickListener {
            pageImageCallback.onClick()
        })

        container.addView(image_layout)
        return image_layout
    }

    init {
        this.PageList = PageList
        inflater = LayoutInflater.from(context)
    }
}