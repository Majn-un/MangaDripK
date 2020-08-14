package com.example.mangadripk.Activity

//import android.app.ProgressDialog
import com.example.mangadripk.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.mangadripk.Adapter.PageViewAdapter
import com.example.mangadripk.Classes.Page

import com.example.mangadripk.Sources.Sources
import com.example.mangadripk.Sources.manga.MangaHere.extractSecretKey
import com.programmersbox.manga_sources.mangasources.ChapterModel
import com.squareup.duktape.Duktape
import com.squareup.okhttp.OkHttpClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.*

class Page_Activity : AppCompatActivity() {
    private var model: ChapterModel? = null
    lateinit var lstPages: MutableList<Page>
    private val chapter_title: TextView? = null
    private var Chapter_URL: String? = null
    private var myViewPager: PageViewAdapter? = null
    private var Cookie1: String? = null
    private var Cookie2: String? = null
    private val doc: Document? = null

    //    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)
//        progressDialog = ProgressDialog(this@Page_Activity)
//        progressDialog!!.show()
//        progressDialog.setContentView(R.layout.progress_dialog)
//        progressDialog!!.setCancelable(false)
//        progressDialog!!.window!!.setBackgroundDrawableResource(R.color.transparent)
        val intent = intent
        val Title = intent.getStringExtra("Name")
        Chapter_URL = intent.getStringExtra("Link")
//        Cookie1 = intent.getStringExtra("ci_session")
//        Cookie2 = intent.getStringExtra("__cfduid")
        lstPages = ArrayList()
        mangaPages()
        val myrv = findViewById<View>(R.id.view_page) as ViewPager
        myViewPager = PageViewAdapter(this, lstPages)
        myrv.adapter = myViewPager
    }

    private fun mangaPages() {
//        refresh.isRefreshing = true
        GlobalScope.launch {
            try {
                val docments = Jsoup.connect(Chapter_URL).get()
                val bar = docments.select("script[src*=chapter_bar]")
                val duktape = Duktape.create()


                // if-branch is for webtoon reader, else is for page-by-page
                    if (bar.isNotEmpty()) {
                        val script =
                            docments.select("script:containsData(function(p,a,c,k,e,d))").html()
                                .removePrefix("eval")
                        val deobfuscatedScript = duktape.evaluate(script).toString()
                        val urls =
                            deobfuscatedScript.substringAfter("newImgs=['").substringBefore("'];")
                                .split("','")
                        duktape.close()

                        urls.map { s -> "https:$s" }
                    } else {
                        val html = docments.html()
                        val link = docments.location()

                        var secretKey = extractSecretKey(html, duktape)

                        val chapterIdStartLoc = html.indexOf("chapterid")
                        val chapterId = html.substring(
                            chapterIdStartLoc + 11,
                            html.indexOf(";", chapterIdStartLoc)
                        ).trim()

                        val chapterPagesElement = docments.select(".pager-list-left > span").first()
                        val pagesLinksElements = chapterPagesElement.select("a")
                        val pagesNumber =
                            pagesLinksElements[pagesLinksElements.size - 2].attr("data-page")
                                .toInt()

                        val pageBase = link.substring(0, link.lastIndexOf("/"))

                        IntRange(1, pagesNumber).map { i ->

                            val pageLink =
                                "$pageBase/chapterfun.ashx?cid=$chapterId&page=$i&key=$secretKey"

                            val responseText = ""
                            println(pageLink)
                            for (tr in 1..3) {

                                val request = Request.Builder()
                                    .url(pageLink)
                                    .addHeader("Referer", link)
                                    .addHeader("Accept", "*/*")
                                    .addHeader("Accept-Language", "en-US,en;q=0.9")
                                    .addHeader("Connection", "keep-alive")
                                    .addHeader("Host", "www.mangahere.cc")
                                    .addHeader("User-Agent", System.getProperty("http.agent") ?: "")
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    .build()

//                                val response = OkHttpClient().newCall(request).execute()
//                                responseText = response.body?.string().toString()

                                if (responseText.isNotEmpty())
                                    break
                                else
                                    secretKey = ""
                            }

//                            val deobfuscatedScript =
//                                duktape.evaluate(responseText.removePrefix("eval")).toString()
//
//                            val baseLinkStartPos = deobfuscatedScript.indexOf("pix=") + 5
//                            val baseLinkEndPos =
//                                deobfuscatedScript.indexOf(";", baseLinkStartPos) - 1
//                            val baseLink =
//                                deobfuscatedScript.substring(baseLinkStartPos, baseLinkEndPos)
//
//                            val imageLinkStartPos = deobfuscatedScript.indexOf("pvalue=") + 9
//                            val imageLinkEndPos =
//                                deobfuscatedScript.indexOf("\"", imageLinkStartPos)
//                            val imageLink =
//                                deobfuscatedScript.substring(imageLinkStartPos, imageLinkEndPos)
//
//                            "https:$baseLink$imageLink"
                        }
                    }
//                        .dropLastIfBroken()
                        .also { duktape.close() }
            } catch (ignored: InterruptedException) {
                Log.d("Yuh", "Something is not working")
            }
        }

        fun extractSecretKey(html: String, duktape: Duktape): String {

            val secretKeyScriptLocation = html.indexOf("eval(function(p,a,c,k,e,d)")
            val secretKeyScriptEndLocation = html.indexOf("</script>", secretKeyScriptLocation)
            val secretKeyScript =
                html.substring(secretKeyScriptLocation, secretKeyScriptEndLocation)
                    .removePrefix("eval")

            val secretKeyDeobfuscatedScript = duktape.evaluate(secretKeyScript).toString()

            val secretKeyStartLoc = secretKeyDeobfuscatedScript.indexOf("'")
            val secretKeyEndLoc = secretKeyDeobfuscatedScript.indexOf(";")

            val secretKeyResultScript = secretKeyDeobfuscatedScript.substring(
                secretKeyStartLoc, secretKeyEndLoc
            )

            return duktape.evaluate(secretKeyResultScript).toString()
        }
    }
}


//                mangaList.addAll(list)
////                println(list[1])
//                runOnUiThread {
//                    myAdapter?.notifyDataSetChanged()
////                    adapter2.addItems(list)
////                    search_layout.suffixText = "${mangaList.size}"
////                }
//            } catch (e: Exception) {
//                e.printStackTrace()
////                FirebaseCrashlytics.getInstance().log("$currentSource had an error")
////                FirebaseCrashlytics.getInstance().recordException(e)
////                Firebase.analytics.logEvent("manga_load_error") {
////                    param(FirebaseAnalytics.Param.ITEM_NAME, currentSource.name)
//            }
////                runOnUiThread {
////                    MaterialAlertDialogBuilder(this@MainActivity)
////                        .setTitle(R.string.wentWrong)
////                        .setMessage(getString(R.string.wentWrongInfo, currentSource.name))
////                        .setPositiveButton(R.string.ok) { d, _ -> d.dismiss() }
////                        .show()
////                }
//        }
//    }
//}

    //
    //                           .cookies(cookies)


//    private fun mangaPages() {
//            Thread(Runnable {
//                try {
////                    val cookies =
////                        LinkedHashMap<String, String?>()
////                    cookies["__cfduid"] = Cookie2
////                    cookies["ci_session"] = Cookie1
////                    Log.d("page", cookies.toString() + "")
//                    val doc =
//                        Jsoup.connect(Chapter_URL) //                            .cookies(cookies)
//                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
//                            .referrer(Chapter_URL)
//                            .get()
//                    val link_list =
//                        doc.select("div.container-chapter-reader").select("img")
//                    val length = link_list.size
//                    val pattern = "\\//(.*?)\\/"
//                    if (length == 0) {
//                        val Mangakakalot =
//                            doc.select("div.vung-doc").select("img")
//                        Log.d("Manga", Mangakakalot.toString() + "")
//                        for (i in Mangakakalot.indices) {
//                            val image_url = Mangakakalot.eq(i).attr("src")
//                            Log.d("Mangakakalot", image_url)
//                            val reincarnatedURL =
//                                image_url.replace(pattern.toRegex(), "//s8.mkklcdnv8.com/")
//                            val Page_Number = (i + 1).toString()
////                            Log.d("Page Link", reincarnatedURL)
//                            lstPages.add(Page(reincarnatedURL, Page_Number))
//                        }
//                    } else {
//                        val Manganelo =
//                            doc.select("div.container-chapter-reader").select("img")
//                        for (i in Manganelo.indices) {
//                            val image_url = Manganelo.eq(i).attr("src")
//                            val reincarnatedURL =
//                                image_url.replace(pattern.toRegex(), "//s8.mkklcdnv8.com/")
//                            Log.d("manganelo", image_url)
//                            val Page_Number = (i + 1).toString()
////                            Log.d("Page Link", reincarnatedURL)
//
//                            lstPages.add(Page(reincarnatedURL, Page_Number))
//                        }
//                    }
////                    progressDialog!!.dismiss()
//                } catch (ignored: IOException) {
//                    Log.d("Yuh", "Something is not working")
//                }
//                runOnUiThread { myViewPager?.notifyDataSetChanged() }
//            }).start()
//        }