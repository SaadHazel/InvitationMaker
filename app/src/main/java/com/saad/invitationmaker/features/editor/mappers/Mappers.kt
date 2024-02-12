package com.saad.invitationmaker.features.editor.mappers

import com.saad.invitationmaker.features.editor.models.SingleStickerUrl
import com.saad.invitationmaker.features.editor.models.stickers.Data
import com.saad.invitationmaker.features.editor.models.stickers.SingleStickerUrlList

/*fun List<Data>.toMyData(): SingleStickerList {
    val images: List<String> = this.map { it.image.source.url }
    val licenses: List<String> = this.flatMap { it.licenses.map { license -> license.type } }

    return SingleStickerUrl(images, licenses)
}*/

fun List<Data>.toMyData(): SingleStickerUrlList {
    val singleStickerUrls = this.map { data ->
        SingleStickerUrl(data.image.source.url, data.licenses.joinToString(", ") { it.type })
    }
    return SingleStickerUrlList(singleStickerUrls)
}