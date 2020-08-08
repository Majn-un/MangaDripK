package com.example.mangadripk.Classes

class Manga {
    var title: String? = null
    var description: String? = null
    var thumbnail: String? = null

    constructor() {}
    constructor(title: String?, description: String?, thumbnail: String?) {
        this.title = title
        this.description = description
        this.thumbnail = thumbnail
    }

}
