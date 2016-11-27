package com.andreapivetta.blu.data.url

import com.schinizer.rxunfurl.RxUnfurl
import com.schinizer.rxunfurl.model.PreviewData
import rx.Observable
import rx.schedulers.Schedulers


/**
 * Created by andrea on 27/11/16.
 */
object UrlInfo {

    val rxUrlInfo: RxUnfurl = RxUnfurl.Builder()
            .scheduler(Schedulers.io())
            .build()

    fun generatePreview(url: String): Observable<PreviewData> = rxUrlInfo.generatePreview(url)

}