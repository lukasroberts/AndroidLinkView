package com.fresh.materiallinkpreview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fresh.materiallinkpreview.R
import com.fresh.materiallinkpreview.models.OpenGraphMetaData

private val MediumPadding = 16.dp
private val SmallPadding = 8.dp
private val LargePadding = 24.dp

@Preview
@Composable
fun CardLinkPreview(openGraphMetaData: OpenGraphMetaData = OpenGraphMetaData(title = "BBC News", url = "https://www.bbc.com", imageUrl = "", type = ""),
                    materialTheme: MaterialTheme = MaterialTheme) {
    MaterialTheme(
        colors = materialTheme.colors,
        typography = materialTheme.typography,
        shapes = materialTheme.shapes
    ) {
        Card(modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            elevation = 10.dp)
        {
            Row {
                Image(
                    painter = painterResource(R.drawable.ic_baseline_photo_24),
                    contentDescription = "Contact profile picture"
                )
                Column {
                    Text(text = openGraphMetaData.title, style = MaterialTheme.typography.body1,  modifier = Modifier.fillMaxWidth().padding(top = SmallPadding, bottom = 0.dp, start = 0.dp, end = 0.dp))
                    Text(text = openGraphMetaData.url, style = MaterialTheme.typography.caption, modifier = Modifier.fillMaxWidth().padding(top = 2.dp, bottom = 0.dp, start = 0.dp, end = 0.dp), color = Color.Gray)
                    Text(text = openGraphMetaData.url, style = MaterialTheme.typography.caption, modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 6.dp, start = 0.dp, end = 0.dp), fontSize = 8.sp)
                }
            }
        }
    }
}

