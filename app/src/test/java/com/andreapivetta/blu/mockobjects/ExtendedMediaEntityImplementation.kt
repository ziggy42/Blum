package com.andreapivetta.blu.mockobjects

import twitter4j.ExtendedMediaEntity
import twitter4j.MediaEntity

/**
 * Created by andrea on 21/07/16.
 */
class ExtendedMediaEntityImplementation : ExtendedMediaEntity {

    var mockVideoAspectRatioHeight: Int = 0
    var mockVideoAspectRatioWidth: Int = 0
    var mockVideoDurationInMillis: Long = 0L
    var mockVideoVariants: Array<out ExtendedMediaEntity.Variant>? = null
    var mockURL: String? = null
    var mockEnd: Int = 0
    var mockText: String? = null
    var mockStart: Int = 0
    var mockDisplayURL: String? = null
    var mockExpandedURL: String? = null
    var mockId: Long = 0L
    var mockType: String? = null
    var mockMediaURLHttps: String? = null
    var mockSizes: MutableMap<Int, MediaEntity.Size>? = null
    var mockMediaURL: String? = null

    override fun getVideoAspectRatioHeight() = mockVideoAspectRatioHeight

    override fun getVideoAspectRatioWidth() = mockVideoAspectRatioWidth

    override fun getVideoDurationMillis() = mockVideoDurationInMillis

    override fun getVideoVariants() = mockVideoVariants

    override fun getURL() = mockURL

    override fun getEnd() = mockEnd

    override fun getText() = mockText

    override fun getStart() = mockStart

    override fun getDisplayURL() = mockDisplayURL

    override fun getExpandedURL() = mockExpandedURL

    override fun getId() = mockId

    override fun getType() = mockType

    override fun getMediaURLHttps() = mockMediaURLHttps

    override fun getSizes() = mockSizes

    override fun getMediaURL() = mockMediaURL

    override fun getExtAltText(): String {
        throw UnsupportedOperationException("not implemented")
    }
}