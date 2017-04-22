package com.andreapivetta.blu.data.url

import com.schinizer.rxunfurl.RxUnfurl
import com.schinizer.rxunfurl.model.PreviewData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


/**
 * Created by andrea on 27/11/16.
 */
object UrlInfo {

    val rxUrlInfo = RxUnfurl.Builder()
            .scheduler(Schedulers.io())
            .build()

    fun generatePreview(url: String): Observable<PreviewData> = rxUrlInfo.generatePreview(url)

}