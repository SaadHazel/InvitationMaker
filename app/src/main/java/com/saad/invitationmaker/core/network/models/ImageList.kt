package com.saad.invitationmaker.core.network.models

data class ImageList(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int,
)