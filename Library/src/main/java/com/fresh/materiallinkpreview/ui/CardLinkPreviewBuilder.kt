package com.fresh.materiallinkpreview.ui

import androidx.compose.ui.graphics.painter.Painter

/**
 * The properties for generating a CardLinkPreview object
 *
 * @property drawWithCardOutline - whether the preview should draw with a card around it
 * @property maxNumberOfLinesForTitle - the maximum number of lines for the title
 * @property maxNumberOfLinesForDescription - the maximum number of lines for the description
 * @property maxNumberOfLinesForUrl - the maximum number of lines for the URL
 * @property imagePainter - the painter for the image that you wish to render on the card
 */
class CardLinkPreviewProperties private constructor(
    val drawWithCardOutline : Boolean,
    val maxNumberOfLinesForTitle: Int,
    val maxNumberOfLinesForDescription: Int,
    val maxNumberOfLinesForUrl: Int,
    val imagePainter: Painter?
) {
    /**
     * The builder that allows you to construct properties for generating a CardLinkPreview object
     *
     * @property drawWithCardOutline - whether the preview should draw with a card around it, defaults to true
     * @property maxNumberOfLinesForTitle - the maximum number of lines for the title, defaults to 2
     * @property maxNumberOfLinesForDescription - the maximum number of lines for the description, defaults to 3
     * @property maxNumberOfLinesForUrl - the maximum number of lines for the URL, defaults to 1
     * @property imagePainter - the painter for the image that you wish to render on the card
     */
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

