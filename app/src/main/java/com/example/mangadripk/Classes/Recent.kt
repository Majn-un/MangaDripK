package com.example.mangadripk.Classes

class Recent {
    var title: String? = null
    var chapter: String? = null
    var thumbnail: String? = null
    var Link: String? = null


    constructor() {}
    constructor(title: String?, chapter: String?, thumbnail: String?, link: String?) {
        this.title = title
        this.chapter = chapter
        this.thumbnail = thumbnail
        this.Link = link
    }

}
