package com.saad.invitationmaker.core.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Hit(
    val comments: Int,
    val downloads: Int,
    val collections: Int,
    val id: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    val user_id: Int,
    val views: Int,
    val webformatHeight: Int,
    val webformatURL: String,
    val webformatWidth: Int,
) : Parcelable