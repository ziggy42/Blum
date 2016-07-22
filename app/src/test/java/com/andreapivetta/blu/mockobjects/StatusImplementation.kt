package com.andreapivetta.blu.mockobjects

import twitter4j.*
import java.util.*

/**
 * Created by andrea on 21/07/16.
 */
class StatusImplementation : Status {

    var mockContributors: LongArray? = null
    var mockFavorite: Boolean = false
    var mockInReplayToScreenName: String? = null
    var mockGeoLocation: GeoLocation? = null
    var mockSource: String? = null
    var mockInReplayToUserId = 0L
    var mockId = 0L
    var mockWithheldInCountries: Array<out String>? = null
    var mockCurrentUserRetweetId: Long = 0L
    var mockText: String? = null
    var mockInReplayToStatusId: Long = 0L
    var mockPlace: Place? = null
    var mockIsRetweetedByMe: Boolean = false
    var mockUser: User? = null
    var mockRetweeted: Boolean = false
    var mockLang: String? = null
    var mockQuotedStatus: Status? = null
    var mockQuotedStatusId: Long = 0L
    var mockRetweet: Boolean = false
    var mockRetweetedStatus: Status? = null
    var mockFavoriteCount: Int = 0
    var mockPossibleSensitive: Boolean = false
    var mockScopes: Scopes? = null
    var mockTruncated: Boolean = false
    var mockCreatedAt: Date? = null
    var mockRetweetCount: Int = 0
    var mockUserMentionEntities: Array<out UserMentionEntity>? = null
    var mockSymbolEntities: Array<out SymbolEntity>? = null
    var mockMediaEntities: Array<out MediaEntity>? = null
    var mockUrlEntities: Array<out URLEntity>? = null
    var mockExtendedMediaEntities: Array<out ExtendedMediaEntity>? = null
    var mockHashtagEntities: Array<out HashtagEntity>? = null
    var mockAccessLevel: Int = 0
    var mockRateLimitStatus: RateLimitStatus? = null

    override fun getContributors() = mockContributors

    override fun isFavorited() = mockFavorite

    override fun getInReplyToScreenName() = mockInReplayToScreenName

    override fun getGeoLocation() = mockGeoLocation

    override fun getSource() = mockSource

    override fun getInReplyToStatusId() = mockInReplayToStatusId

    override fun getId() = mockId

    override fun getWithheldInCountries() = mockWithheldInCountries

    override fun getCurrentUserRetweetId() = mockCurrentUserRetweetId

    override fun getText() = mockText

    override fun getInReplyToUserId() = mockInReplayToUserId

    override fun getPlace() = mockPlace

    override fun isRetweetedByMe() = mockIsRetweetedByMe

    override fun getUser() = mockUser

    override fun isRetweeted() = mockRetweeted

    override fun getLang() = mockLang

    override fun getQuotedStatus() = mockQuotedStatus

    override fun getQuotedStatusId() = mockQuotedStatusId

    override fun isRetweet() = mockRetweet

    override fun getRetweetedStatus() = mockRetweetedStatus

    override fun getFavoriteCount() = mockFavoriteCount

    override fun isPossiblySensitive() = mockPossibleSensitive

    override fun getScopes() = mockScopes

    override fun isTruncated() = mockTruncated

    override fun getCreatedAt() = mockCreatedAt

    override fun getRetweetCount() = mockRetweetCount

    override fun getUserMentionEntities() = mockUserMentionEntities

    override fun getSymbolEntities() = mockSymbolEntities

    override fun getMediaEntities() = mockMediaEntities

    override fun getURLEntities() = mockUrlEntities

    override fun getExtendedMediaEntities() = mockExtendedMediaEntities

    override fun getHashtagEntities() = mockHashtagEntities

    override fun getAccessLevel() = mockAccessLevel

    override fun getRateLimitStatus() = mockRateLimitStatus

    override fun compareTo(other: Status?): Int {
        throw UnsupportedOperationException("not implemented")
    }

}