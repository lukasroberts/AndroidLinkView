package com.fresh.materiallinkpreview.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.fresh.materiallinkpreview.R
import com.fresh.materiallinkpreview.models.OpenGraphMetaData

@Preview
@Composable
private fun PreviewCardLink() {
    MaterialTheme(
        colors = MaterialTheme.colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes
    ) {
        CardLinkPreview(
            OpenGraphMetaData(
                title = "Find the cheapest PCR tests for traveling overseas",
                description = "Not sure about which covid tests you need to travel abroad. Some destinations require you to take a test before leaving the UK and can be expensive for families. Use money saving experts tips to lower costs.",
                url = "www.moneysavingexpert.com",
                imageUrl = "",
                type = ""
            ),
            CardLinkPreviewProperties.Builder(
                true,
                2,
                5,
                1,
                painterResource(R.drawable.ic_baseline_photo_24)
            ).build()
        )
    }
}

@Composable
fun CardLinkPreview(
    openGraphMetaData: OpenGraphMetaData,
    cardLinkPreviewProperties: CardLinkPreviewProperties = CardLinkPreviewProperties.Builder().build()
) {
    val context = LocalContext.current

    if (cardLinkPreviewProperties.drawWithCardOutline) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(openGraphMetaData.url))
                    startActivity(context, browserIntent, null)
                },
            elevation = 10.dp
        ) {
            BaseCardLinkPreview(openGraphMetaData, cardLinkPreviewProperties)
        }
    } else {
        BaseCardLinkPreview(openGraphMetaData, cardLinkPreviewProperties)
    }
}

@Composable
fun BaseCardLinkPreview(
    openGraphMetaData: OpenGraphMetaData,
    cardLinkPreviewProperties: CardLinkPreviewProperties
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        if (cardLinkPreviewProperties.imagePainter != null) {
            Image(
                painter = cardLinkPreviewProperties.imagePainter,
                contentDescription = stringResource(R.string.link_photo),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(0.2f)
                    .height(50.dp)
            )
        }
        Column {
            Text(
                text = openGraphMetaData.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = cardLinkPreviewProperties.maxNumberOfLinesForTitle,
                overflow = TextOverflow.Ellipsis
            )

            val description = openGraphMetaData.description
            if (!description.isNullOrEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
                    color = Color.Gray,
                    maxLines = cardLinkPreviewProperties.maxNumberOfLinesForDescription,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = openGraphMetaData.url,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
                fontSize = 8.sp,
                maxLines = cardLinkPreviewProperties.maxNumberOfLinesForUrl,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

