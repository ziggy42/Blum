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
                val tweetInfoListIds = tweetInfoList.map { it.id }

                newTweetInfoList.filterNot { tweetInfoListIds.contains(it.id) }
                        .forEach {
                            run {
                                Timber.d("New tweet discovered %d", it.id)
                                storage.saveTweetInfo(it)
                                if (it.hasFavoriters() || it.hasRetweeters()) {
                                    val status = twitter.showStatus(it.id)

                                    it.favoriters?.forEach {
                                        dispatcher.sendFavoriteNotification(status,
                                                twitter.showUser(it.userId))
                                    }

                                    it.retweeters?.forEach {
                                        dispatcher.sendRetweetNotification(status,
                                                twitter.showUser(it.userId))
                                    }
                                }
                            }
                        }

                newTweetInfoList.filter { tweetInfoListIds.contains(it.id) }
                        .forEach {
                            run {
                                val savedInfo = tweetInfoList.find { y -> y == it }

                                if (it.favoriters != null) {
                                    val newFav = if (savedInfo != null)
                                        it.favoriters!!.toMutableList()
                                                .filter { savedInfo.favoriters!!.contains(it) }
                                    else it.favoriters

                                    if (newFav != null && newFav.isNotEmpty()) {
                                        storage.saveTweetInfo(savedInfo!!,
                                                { it.favoriters?.addAll(newFav) })
                                        val status = twitter.showStatus(savedInfo.id)
                                        newFav.forEach {
                                            dispatcher.sendFavoriteNotification(status,
                                                    twitter.showUser(it.userId))
                                        }
                                    }
                                }

                                if (it.retweeters != null) {
                                    val newRet = if (savedInfo != null)
                                        it.favoriters?.toMutableList()
                                                ?.filter { savedInfo.favoriters!!.contains(it) }
                                    else it.favoriters

                                    if (newRet != null && newRet.isNotEmpty()) {
                                        storage.saveTweetInfo(savedInfo!!,
                                                { it.retweeters?.addAll(newRet) })
                                        val status = twitter.showStatus(savedInfo.id)
                                        newRet.forEach {
                                            dispatcher.sendRetweetNotification(status,
                                                    twitter.showUser(it.userId))
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
                    savedMentions.contains(it.tweetId) || it.userId == loggedUserId
                }.forEach {
                    run {
                        storage.saveMention(it)
                        dispatcher.sendMentionNotification(
                                twitter.showStatus(it.tweetId), twitter.showUser(it.userId))
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

                newFollowers.filterNot { savedFollowers.contains(it.userId) }
                        .forEach {
                            run {
                                storage.saveFollower(it)
                                dispatcher.sendFollowerNotification(twitter.showUser(it.userId))
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
                        .map { PrivateMessage.valueOf(it, settings.getLoggedUserId()) }
                val savedPrivateMessages = storage.getAllPrivateMessages().map { it.id }

                newPrivateMessages.filterNot { savedPrivateMessages.contains(it.id) }
                        .forEach {
                            run {
                                storage.savePrivateMessage(it)
                                dispatcher.sendPrivateMessageNotification(it)
                            }
                        }
            } else {
                Timber.d("Downloading private messages...")
                storage.savePrivateMessages(NotificationsDataProvider
                        .retrieveDirectMessages(twitter)
                        .map { PrivateMessage.valueOf(it, settings.getLoggedUserId()) })
                settings.setDirectMessagesDownloaded(true)
            }
        }

        storage.close()
        Timber.d("Done in ${(System.currentTimeMillis() - time) / 1000} seconds")
    }

}