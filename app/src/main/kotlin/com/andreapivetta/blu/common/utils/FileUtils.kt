package com.andreapivetta.blu.common.utils

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * I don't remember where this class comes from
 */
object FileUtils {

    private fun isExternalStorageDocument(uri: Uri) =
            "com.android.externalstorage.documents" == uri.authority

    private fun isDownloadsDocument(uri: Uri) =
            "com.android.providers.downloads.documents" == uri.authority

    private fun isMediaDocument(uri: Uri) =
            "com.android.providers.media.documents" == uri.authority

    private fun isGooglePhotosUri(uri: Uri) =
            "com.google.android.apps.photos.contentprovider" == uri.authority

    private fun getDataColumn(context: Context, uri: Uri, selection: String?,
                              selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }

    fun getPath(context: Context, uri: Uri): String? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            @TargetApi(19)
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true))
                        return "${Environment.getExternalStorageDirectory()}/${split[1]}"

                } else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                    val type = split[0]
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    when (type) {
                        "image" -> return getDataColumn(context, MediaStore
                                .Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs)
                        "video" -> return getDataColumn(context, MediaStore
                                .Video.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs)
                        "audio" -> return getDataColumn(context, MediaStore
                                .Audio.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs)
                    }
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                if (isGooglePhotosUri(uri)) {
                    return uri.encodedPath
                }

                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            if (isGooglePhotosUri(uri))
                return uri.lastPathSegment

            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

}
