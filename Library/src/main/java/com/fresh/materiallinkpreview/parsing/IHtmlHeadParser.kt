package com.fresh.materiallinkpreview.parsing

import java.net.URL

interface IHtmlHeadParser {
    /**
     * Retrieves a HTML header for a given link
     *
     * @param link - the link to get the HTML for
     * @return A HTML header
     */
    fun getHtmlHeader(link: URL) : String
}