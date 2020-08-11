package com.example.mangadripk.Classes

import java.io.Serializable


class Chapter : Serializable {
    var name: String? = null
    var link: String? = null
//    var cookie1: String? = null
//    var cookie2: String? = null

    //    private Map<String, String> Cookies;
    constructor() {}
    constructor(
        Chapter_Title: String?,
        Chapter_Link: String?
//        cookie1: String?,
//        cookie2: String?
    ) {
        name = Chapter_Title
        link = Chapter_Link
//        this.cookie1 = cookie1
//        this.cookie2 = cookie2
    }

}