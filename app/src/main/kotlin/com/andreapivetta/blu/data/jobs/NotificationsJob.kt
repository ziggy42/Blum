package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.BuildConfig
import com.andreapivetta.blu.common.settings.AppSettingsFactory
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorageFactory
import com.andreapivetta.blu.data.twitter.TwitterUtils
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 17/09/16.
 */
class NotificationsJob : Job() {

    // TODO performance

    companion object {
        val TAG = "notifications_job"

        fun scheduleJob() {
            JobRequest.Builder(TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(BuildConfig.INTERVAL))
                    .setPersisted(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .build()
                    .schedule()
        }
    }

    override fun onRunJob(params: Params?): Result {
        val settings = AppSettingsFactory.getAppSettings(context)
        val storage = AppStorageFactory.getAppStorage(context)
        val twitter = TwitterUtils.getTwitter()
        val dispatcher = NotificationDispatcher(context, storage)

        if (settings.isNotifyFavRet()) {
            if (settings.isFavRetDownloaded()) {
                Timber.d("Checking for new favorites and retweeets...")
                val newTweetInfoList = NotificationsDataProvider.retrieveTweetInfo(twitter)
                val tweetInfoList = storage.getAllTweetInfo()
                val tweetInfoListIds = tweetInfoList.map { x -> x.id }

                newTweetInfoList.filterNot { x -> tweetInfoListIds.contains(x.id) }
                        .forEach { x ->
                            run {
                                Timber.d("New tweet discovered %d", x.id)
                                storage.saveTweetInfo(x)
                                if (x.favoriters!!.size > 0 || x.retweeters!!.size > 0) {
                                    val status = twitter.showStatus(x.id)

                                    x.favoriters?.forEach { y ->
                                        dispatcher.sendFavoriteNotification(status,
                                                twitter.showUser(y.userId))
                                    }

                                    x.favoriters?.forEach { y ->
                                        dispatcher.sendRetweetNotification(status,
                                                twitter.showUser(y.userId))
                                    }
                                }
                            }
                        }

                newTweetInfoList.filter { x -> tweetInfoListIds.contains(x.id) }
                        .forEach { x ->
                            run {
                                val savedInfo = tweetInfoList.find { y -> y.equals(x) }

                                if (x.favoriters != null) {
                                    val newFav = if (savedInfo != null)
                                        x.favoriters!!.toMutableList()
                                                .filter { x -> savedInfo.favoriters!!.contains(x) }
                                    else x.favoriters

                                    if (newFav != null && newFav.size > 0) {
                                        storage.saveTweetInfo(savedInfo!!,
                                                { savedInfo -> savedInfo.favoriters?.addAll(newFav) })
                                        val status = twitter.showStatus(savedInfo.id)
                                        newFav.forEach { x ->
                                            dispatcher.sendFavoriteNotification(status,
                                                    twitter.showUser(x.userId))
                                        }
                                    }
                                }

                                if (x.retweeters != null) {
                                    val newRet = if (savedInfo != null)
                                        x.favoriters!!.toMutableList()
                                                .filter { x -> savedInfo.favoriters!!.contains(x) }
                                    else x.favoriters

                                    if (newRet != null && newRet.size > 0) {
                                        storage.saveTweetInfo(savedInfo!!,
                                                { savedInfo -> savedInfo.retweeters?.addAll(newRet) })
                                        val status = twitter.showStatus(savedInfo.id)
                                        newRet.forEach { x ->
                                            dispatcher.sendRetweetNotification(status,
                                                    twitter.showUser(x.userId))
                                        }
                                    }
                                }

                            }
                        }
            } else {
                Timber.d("Downloading tweets information...")
                storage.saveTweetInfoList(NotificationsDataProvider.retrieveTweetInfo(twitter))
                settings.setFavRetDownloaded(true)
            }
        }

        if (settings.isNotifyMentions()) {
            if (settings.isMentionsDownloaded()) {
                Timber.d("Checking for new mentions...")
                val newMentions = NotificationsDataProvider.retrieveMentions(twitter)
                val savedMentions = storage.getAllMentions().map { x -> x.tweetId }

                newMentions.filterNot { x -> savedMentions.contains(x.tweetId) }
                        .forEach { x ->
                            run {
                                storage.saveMention(x)
                                dispatcher.sendMentionNotification(
                                        twitter.showStatus(x.tweetId), twitter.showUser(x.userId))
                            }
                        }
            } else {
                Timber.d("Downloading mentions...")
                storage.saveMentions(NotificationsDataProvider.retrieveMentions(twitter))
                settings.setMentionsDownloaded(true)
            }
        }

        if (settings.isNotifyFollowers()) {
            if (settings.isFollowersDownloaded()) {
                Timber.d("Checking for new followers...")
                val newFollowers = NotificationsDataProvider.retrieveFollowers(twitter)
                val savedFollowers = storage.getAllFollowers().map { x -> x.userId }

                newFollowers.filterNot { x -> savedFollowers.contains(x.userId) }
                        .forEach { x ->
                            run {
                                storage.saveFollower(x)
                                dispatcher.sendFollowerNotification(twitter.showUser(x.userId))
                            }
                        }
            } else {
                Timber.d("Downloading followers...")
                storage.saveFollowers(NotificationsDataProvider.retrieveFollowers(twitter))
                settings.setFollowersDownloaded(true)
            }
        }

        if (settings.isNotifyDirectMessages()) {
            if (settings.isDirectMessagesDownloaded()) {
                Timber.d("Checking for new private messages...")
                val newPrivateMessages = NotificationsDataProvider.retrieveDirectMessages(twitter)
                        .map { x -> PrivateMessage.valueOf(x, settings.getLoggedUserId()) }
                val savedPrivateMessages = storage.getAllPrivateMessages().map { x -> x.id }

                newPrivateMessages.filterNot { x -> savedPrivateMessages.contains(x.id) }
                        .forEach { x ->
                            run {
                                storage.savePrivateMessage(x)
                                dispatcher.sendPrivateMessageNotification(x)
                            }
                        }
            } else {
                Timber.d("Downloading private messages...")
                storage.savePrivateMessages(NotificationsDataProvider
                        .retrieveDirectMessages(twitter)
                        .map { x -> PrivateMessage.valueOf(x, settings.getLoggedUserId()) })
                settings.setDirectMessagesDownloaded(true)
            }
        }

        Timber.d("Done")

        return Result.SUCCESS
    }
}