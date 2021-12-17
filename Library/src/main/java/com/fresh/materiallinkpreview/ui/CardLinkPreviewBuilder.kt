package com.fresh.materiallinkpreview.ui

import androidx.compose.ui.graphics.painter.Painter

class CardLinkPreviewProperties private constructor(
    val drawWithCardOutline : Boolean,
    val maxNumberOfLinesForTitle: Int,
    val maxNumberOfLinesForDescription: Int,
    val maxNumberOfLinesForUrl: Int,
    val imagePainter: Painter?
) {
    data class Builder(
        var drawWithCardOutline : Boolean = true,
        var maxNumberOfLinesForTitle: Int = 2,
        var maxNumberOfLinesForDescription: Int = 3,
        var maxNumberOfLinesForUrl: Int = 1,
        var imagePainter: Painter? = null
    ) {
        fun maxNumberOfLinesForTitle(maxNumberOfLinesForTitle: Int) =
            apply { this.maxNumberOfLinesForTitle = maxNumberOfLinesForTitle }

        fun maxNumberOfLinesForDescription(maxNumberOfLinesForDescription: Int) =
            apply { this.maxNumberOfLinesForDescription = maxNumberOfLinesForDescription }

        fun maxNumberOfLinesForUrl(maxNumberOfLinesForUrl: Int) =
            apply { this.maxNumberOfLinesForUrl = maxNumberOfLinesForUrl }

        fun drawWithCardOutline(drawWithCardOutline: Boolean) =
            apply { this.drawWithCardOutline = drawWithCardOutline }

        fun imagePainter(imagePainter: Painter) =
            apply { this.imagePainter = imagePainter }

        fun build() = CardLinkPreviewProperties(
            drawWithCardOutline,
            maxNumberOfLinesForTitle,
            maxNumberOfLinesForDescription,
            maxNumberOfLinesForUrl,
            imagePainter
        )
    }
}

