package com.fresh.materiallinkpreview

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.ui.CardLinkPreview
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardLinkPreviewTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val openGraphMetaData = OpenGraphMetaData(
        title = "Find the cheapest PCR tests for traveling overseas",
        description = "Not sure about which covid tests you need to travel abroad. Some destinations require you to take a test before leaving the UK and can be expensive for families. Use money saving experts tips to lower costs.",
        url = "www.moneysavingexpert.com",
        imageUrl = "",
        type = ""
    )

    @Before
    fun setup() {
        composeTestRule.setContent {
            CardLinkPreview(
                painterResource(R.drawable.ic_baseline_photo_24), openGraphMetaData, MaterialTheme
            )
        }
    }

    @Test
    fun testLoadsContentSuccessfully() {
        composeTestRule.onNodeWithText(openGraphMetaData.title).assertExists()

        if(!openGraphMetaData.description.isNullOrEmpty()) {
            composeTestRule.onNodeWithText(openGraphMetaData.description!!).assertExists()
        }

        composeTestRule.onNodeWithText(openGraphMetaData.url).assertExists()
    }
}