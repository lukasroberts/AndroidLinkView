package com.fresh.materiallinkpreview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            painterResource(R.drawable.ic_baseline_photo_24),
            OpenGraphMetaData(
                title = "Find the cheapest PCR tests for traveling overseas",
                description = "Not sure about which covid tests you need to travel abroad. Some destinations require you to take a test before leaving the UK and can be expensive for families. Use money saving experts tips to lower costs.",
                url = "www.moneysavingexpert.com",
                imageUrl = "",
                type = ""
            )
        )
    }
}

@Composable
fun CardLinkPreview(
    imagePainter: Painter,
    openGraphMetaData: OpenGraphMetaData,
    drawWithCardOutline : Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = 10.dp
    )
    {
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = imagePainter,
                contentDescription = stringResource(R.string.link_photo),
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .height(50.dp)
            )
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
}

