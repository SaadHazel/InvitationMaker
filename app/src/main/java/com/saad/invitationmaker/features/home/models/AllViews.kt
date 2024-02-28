package com.saad.invitationmaker.features.home.models

class AllViews(

) {
    var viewId: String? = null
    var viewData: String? = null
    var alignment: String? = null
    var viewType: String? = null
    var fontSize: String? = null
    var font: String? = null
    var letterSpacing: String? = null
    var lineHeight: String? = null
    var textStyle: String? = null
    var color: String? = null
    var xCoordinate: String? = null
    var yCoordinate: String? = null
    var width: String? = null
    var height: String? = null
    var priority: String? = null

    constructor(
        viewId: String?,
        viewData: String?,
        alignment: String?,
        viewType: String?,
        fontSize: String?,
        font: String?,
        letterSpacing: String?,
        lineHeight: String?,
        textStyle: String?,
        color: String?,
        xCoordinate: String?,
        yCoordinate: String?,
        width: String?,
        height: String?,
        priority: String?,
    ) : this() {
        this.viewId = viewId
        this.viewData = viewData
        this.alignment = alignment
        this.viewType = viewType
        this.fontSize = fontSize
        this.font = font
        this.letterSpacing = letterSpacing
        this.lineHeight = lineHeight
        this.textStyle = textStyle
        this.color = color
        this.xCoordinate = xCoordinate
        this.yCoordinate = yCoordinate
        this.width = width
        this.height = height
        this.priority = priority
    }
}
