package com.fresh.sampleproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.parsing.OpenGraphMetaDataProvider
import com.fresh.materiallinkpreview.ui.CardLinkPreview
import com.fresh.materiallinkpreview.ui.CardLinkPreviewProperties
import com.fresh.sampleproject.ui.theme.MaterialLinkPreviewTheme
//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val viewModel: MainActivityViewModel by viewModels()
    private val viewModel: MainActivityViewModel = MainActivityViewModel(OpenGraphMetaDataProvider())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.downloadMetaData()

        setContent {
            MaterialLinkPreviewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    MainActivityScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen(mainActivityViewModel: MainActivityViewModel) {
    val metaDataList by mainActivityViewModel.metaDataList.observeAsState()
    metaDataList?.let { LinkPreviewList(it) }
}

@Composable
fun LinkPreviewList(metaDataList: List<OpenGraphMetaData>) {

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            items(metaDataList) { metaData ->
                if (metaData.imageUrl.isNotEmpty()) {
                    CardLinkPreview(
                        metaData, CardLinkPreviewProperties.Builder(
                            imagePainter = rememberImagePainter(metaData.imageUrl)
                        ).build()
                    )
                } else {
                    CardLinkPreview(metaData)
                }
            }
        }

        val scrollState = rememberScrollState()

        Card(
            Modifier
                .fillMaxWidth(0.7f)
                .padding(8.dp)
                .scrollable(scrollState, orientation = Orientation.Vertical),
            elevation = 10.dp
        ) {
            Column {
                Text(
                    text = stringResource(R.string.lorem_ipsum),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp)
                )

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                )

                val mse = OpenGraphMetaData(
                    title = "Find the cheapest PCR tests for traveling overseas",
                    description = "Not sure about which covid tests you need to travel abroad. Some destinations require you to take a test before leaving the UK and can be expensive for families. Use money saving experts tips to lower costs.",
                    url = "www.moneysavingexpert.com",
                    imageUrl = "",
                    type = ""
                )

                CardLinkPreview(
                    mse, CardLinkPreviewProperties.Builder(
                        drawWithCardOutline = false
                    ).build()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val mse = OpenGraphMetaData(
        title = "Find the cheapest PCR tests for traveling overseas",
        description = "Not sure about which covid tests you need to travel abroad. Some destinations require you to take a test before leaving the UK and can be expensive for families. Use money saving experts tips to lower costs.",
        url = "www.moneysavingexpert.com",
        imageUrl = "",
        type = ""
    )

    val bbc = OpenGraphMetaData(
        title = "BBC - Home",
        description = "All the latest news that you will ever need.",
        url = "www.bbc.com",
        imageUrl = "",
        type = ""
    )

    val metaList = listOf(mse, bbc)

    MaterialLinkPreviewTheme {
        LinkPreviewList(metaList)
    }
}