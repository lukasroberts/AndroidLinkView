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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            imagePainter = painterResource(R.drawable.ic_baseline_photo_24))
    }
}

@Composable
fun CardLinkPreview(
    openGraphMetaData: OpenGraphMetaData,
    modifier: Modifier = Modifier,
    imagePainter: Painter? = null,
    drawWithCardOutline : Boolean = true
) {
    val context = LocalContext.current

    if(drawWithCardOutline) {
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
            BaseCardLinkPreview(openGraphMetaData, imagePainter)
        }
    } else {
        BaseCardLinkPreview(openGraphMetaData, imagePainter)
    }
}

@Composable
fun BaseCardLinkPreview(openGraphMetaData: OpenGraphMetaData,
                        imagePainter: Painter? = null) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        if(imagePainter != null) {
            Image(
                painter = imagePainter,
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
                    .fillMaxWidth()
            )

            val description = openGraphMetaData.description
            if (!description.isNullOrEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
                    color = Color.Gray
                )
            }

            Text(
                text = openGraphMetaData.url,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
                fontSize = 8.sp
            )
        }
    }
}

