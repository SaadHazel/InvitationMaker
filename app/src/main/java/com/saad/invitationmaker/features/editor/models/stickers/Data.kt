package com.saad.invitationmaker.features.editor.models.stickers

data class Data(
    val active: Boolean,
    val author: Author,
    val filename: String,
    val id: Int,
    val image: Image,
    val licenses: List<License>,
//    val meta: Meta,
    val products: List<Any>,
    val related: Related,
    val stats: Stats,
    val title: String,
    val url: String,
)