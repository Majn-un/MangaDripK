package com.programmersbox.manga_sources.mangasources

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mangadripk.Sources.Sources
import com.example.mangadripk.Sources.utilites.NetworkHelper

interface MangaSource {
    val websiteUrl: String
    val hasMorePages: Boolean
    val headers: List<Pair<String, String>> get() = emptyList()
    fun getManga(pageNumber: Int = 1): List<MangaModel>
    fun toInfoModel(model: MangaModel): MangaInfoModel
    fun getMangaModelByUrl(url: String): MangaModel
    fun getPageInfo(chapterModel: ChapterModel): PageModel
    fun search(string: String): List<MangaModel>
    fun getMangaRanked(pageNumber: Int = 1): List<MangaModel>
    fun getMangaLatest(pageNumber: Int = 1): List<MangaModel>

//    fun searchManga(searchText: CharSequence): List<MangaModel>
}

data class MangaModel(
    val title: String,
    val description: String,
    val mangaUrl: String,
    val imageUrl: String,
    val source: Sources
) : ViewModel() {
    internal val extras = mutableMapOf<String, Any>()
    fun toInfoModel() = source.toInfoModel(this)
}

data class MangaInfoModel(
    val title: String,
    val description: String,
    val mangaUrl: String,
    val imageUrl: String,
    val chapters: List<ChapterModel>,
    val genres: List<String>,
    val alternativeNames: List<String>,
    val author: String,
    val status: String
) : ViewModel()

data class ChapterModel(
    val name: String,
    val url: String,
    val uploaded: String,
    val sources: Sources
) : ViewModel() {
    var uploadedTime: Long? = null
    fun getPageInfo() = sources.getPageInfo(this)
}

data class PageModel(val pages: List<String>)

object MangaContext {
    lateinit var context: Context

    @Volatile
    private var INSTANCE: NetworkHelper? = null

    fun getInstance(context: Context): NetworkHelper = INSTANCE ?: synchronized(this) { INSTANCE ?: NetworkHelper(context).also { INSTANCE = it } }
}
