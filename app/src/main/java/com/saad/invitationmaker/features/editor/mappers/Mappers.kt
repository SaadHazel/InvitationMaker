package com.saad.invitationmaker.features.editor.mappers

import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.editor.models.SingleStickerUrl
import com.saad.invitationmaker.features.editor.models.stickers.Data
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList
import com.saad.invitationmaker.features.home.models.AllViews
import com.saad.invitationmaker.features.home.models.DraftAllViews

/*fun List<Data>.toMyData(): SingleStickerList {
    val images: List<String> = this.map { it.image.source.url }
    val licenses: List<String> = this.flatMap { it.licenses.map { license -> license.type } }

    return SingleStickerUrl(images, licenses)
}*/

fun List<Hit>.toStickerList(): List<String> {
    return map { it.largeImageURL }
}

fun List<Data>.toMyData(): SingleStickerUrlList {
    val singleStickerUrls = this.map { data ->
        SingleStickerUrl(data.image.source.url, data.licenses.joinToString(", ") { it.type })
    }
    return SingleStickerUrlList(singleStickerUrls)
}

fun AllViews.toMyDraftView(): DraftAllViews {
    return DraftAllViews(
        viewId = this.viewId ?: "",
        viewData = this.viewData ?: "",
        alignment = this.alignment,
        viewType = this.viewType,
        fontSize = this.fontSize,
        font = this.font,
        letterSpacing = this.letterSpacing,
        lineHeight = this.lineHeight,
        textStyle = this.textStyle,
        color = this.color,
        xCoordinate = this.xCoordinate,
        yCoordinate = this.yCoordinate,
        width = this.width,
        height = this.height,
        priority = this.priority
    )
}