package com.saad.invitationmaker.features.editor.models

import android.view.View

data class LayersModel(
    val view: View,
    val viewData: String,
    var priority: Int,
    val lock: Boolean,
    val hide: Boolean,
)