package com.example.mangadripk.Classes

import com.example.mangadripk.Sources.Sources
import java.io.Serializable


class Chapter : Serializable {
    var name: String? = null
    var link: String? = null
    var sources: Sources? = null
    var uploadedTime: Long? = null
    var uploaded: String? = null

    constructor(
        Chapter_Title: String?,
        Chapter_Link: String?,
        Chapter_Sources: Sources?,
        Chapter_uploaded: String?,
        Chapter_upload: Long?

    ) {
        name = Chapter_Title
        link = Chapter_Link
        uploaded = Chapter_uploaded
        uploadedTime = Chapter_upload
        sources = Chapter_Sources
    }

}