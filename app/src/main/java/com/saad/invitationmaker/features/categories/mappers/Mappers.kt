package com.saad.invitationmaker.features.categories.mappers

import com.saad.invitationmaker.core.network.models.Hit
import com.saad.invitationmaker.features.categories.models.BackgroundDesigns

fun List<Hit>.toMyData(): BackgroundDesigns {
    val images = this.map { it.largeImageURL }
    return BackgroundDesigns(images)
}