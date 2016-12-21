package com.andreapivetta.blu.data.jobs

import com.andreapivetta.blu.common.settings.AppSettings
import com.andreapivetta.blu.data.model.Follower
import com.andreapivetta.blu.data.model.Mention
import com.andreapivetta.blu.data.model.PrivateMessage
import com.andreapivetta.blu.data.storage.AppStorage
import timber.log.Timber
import twitter4j.Twitter

/**
 * Created by andrea on 07/12/16.
 */
object NotificationsDownloader {

    @Synchronized fun checkNotifications(settings: AppSettings, storage: AppStorage, twitter: Twitter,
                                         dispatcher: NotificationDispatcher) {
        val time = System.currentTimeMillis()

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
                                if (x.hasFavoriters() || x.hasRetweeters()) {
                                    val status = twitter.showStatus(x.id)

                                    x.favoriters?.forEach { y ->
                                        dispatcher.sendFavoriteNotification(status,
                                                twitter.showUser(y.userId))
                                    }

                                    x.retweeters?.forEach { y ->
                                        dispatcher.sendRetweetNotification(status,
                                                twitter.showUser(y.userId))
                                    }
                                }
                            }
                        }

                newTweetInfoList.filter { x -> tweetInfoListIds.contains(x.id) }
                        .forEach { x ->
                            run {
                                val savedInfo = tweetInfoList.find { y -> y == x }

                                if (x.favoriters != null) {
                                    val newFav = if (savedInfo != null)
                                        x.favoriters!!.toMutableList()
                                                .filter { x -> savedInfo.favoriters!!.contains(x) }
                                    else x.favoriters

                                    if (newFav != null && newFav.isNotEmpty()) {
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
                                        x.favoriters?.toMutableList()
                                                ?.filter { x -> savedInfo.favoriters!!.contains(x) }
                                    else x.favoriters

                                    if (newRet != null && newRet.isNotEmpty()) {
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
                val savedMentions = storage.getAllMentions().map(Mention::tweetId)
                val loggedUserId = settings.getLoggedUserId()

                newMentions.filterNot {
                    x ->
                    savedMentions.contains(x.tweetId) || x.userId == loggedUserId
                }.forEach { x ->
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
                val savedFollowers = storage.getAllFollowers().map(Follower::userId)

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

        storage.close()
        Timber.d("Done in ${(System.currentTimeMillis() - time) / 1000} seconds")
    }

}