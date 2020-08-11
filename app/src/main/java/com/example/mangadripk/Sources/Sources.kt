package com.example.mangadripk.Sources

import com.example.mangadripk.Sources.manga.MangaHere

//import com.example.mangadripk.Sources.manga.*

enum class Sources(
    val domain: String,
    val isAdult: Boolean = false,
    val filterOutOfUpdate: Boolean = false,
    val source: MangaHere
) : MangaSource by source {

    //MANGA_EDEN(domain = "mangaeden", filterOutOfUpdate = true, source = MangaEden),
    MANGA_HERE(domain = "mangahere", source = MangaHere);
//    MANGA_4_LIFE(domain = "manga4life", source = MangaFourLife),
//    NINE_ANIME(domain = "nineanime", source = NineAnime),
//    MANGAKAKALOT(domain = "mangakakalot", source = Mangakakalot);
//    MANGAMUTINY(domain = "mangamutiny", source = Mangamutiny),
//    MANGA_PARK(domain = "mangapark", source = MangaPark),

    //MANGA_DOG(domain = "mangadog", source = MangaDog),
//    INKR(domain = "mangarock", source = com.programmersbox.manga_sources.mangasources.manga.INKR),
//    TSUMINO(domain = "tsumino", isAdult = true, source = Tsumino);

    companion object {
        fun getSourceByUrl(url: String) = values().find { url.contains(it.domain, true) }

        fun getUpdateSearches() = values().filterNot(Sources::isAdult).filterNot(Sources::filterOutOfUpdate)
    }
}