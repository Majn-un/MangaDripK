package com.example.mangadripk.Sources

import com.example.mangadripk.Sources.manga.*
import com.programmersbox.manga_sources.mangasources.MangaSource

enum class Sources(
    val domain: String,
    val isAdult: Boolean = false,
    val filterOutOfUpdate: Boolean = false,
    val source: MangaSource
) : MangaSource by source {

    MANGA_4_LIFE(domain = "manga4life", source = MangaFourLife),
    NINE_ANIME(domain = "nineanime", source = NineAnime),
    MANGA_PARK(domain = "mangapark", source = MangaPark, filterOutOfUpdate = true),
    MANGA_HERE(domain = "mangahere", source = MangaHere);


    companion object {
        fun getSourceByUrl(url: String) = values().find { url.contains(it.domain, true) }

        fun getUpdateSearches() = values().filterNot(Sources::isAdult).filterNot(Sources::filterOutOfUpdate)
    }
}