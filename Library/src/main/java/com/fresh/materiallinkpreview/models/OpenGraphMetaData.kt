package com.fresh.materiallinkpreview.models

/**
 * Represents the raw open graph meta data object
 *
 * @property title - The title of your object as it should appear within the graph
 * @property url - The canonical URL of your object that will be used as its permanent ID in the graph, e.g., "https://www.imdb.com/title/tt0117500/"
 * @property imageUrl - An image URL which should represent your object within the graph.
 * @property type - The <a href="https://ogp.me/#types">type</a> of your object, e.g., "video.movie". Depending on the type you specify, other properties may also be required.
 * @property description - A one to two sentence description of your object.
 * @property audioUrl - A URL to an audio file to accompany this object.
 * @property determiner - The word that appears before this object's title in a sentence. An enum of (a, an, the, "", auto). If auto is chosen, the consumer of your data should chose between "a" or "an". Default is "" (blank).
 * @property locale - The locale these tags are marked up in. Of the format language_TERRITORY. Default is en_US.
 * @property alternateLocales - An array of other locales this page is available in.
 * @property siteName - If your object is part of a larger web site, the name which should be displayed for the overall site. e.g., "IMDb".
 * @property videoUrl - A URL to a video file that complements this object.
 */
data class OpenGraphMetaData(var title : String = "",
                             var url : String = "",
                             var imageUrl : String = "",
                             var type : String = "",
                             // Optional parameters
                             var description : String? = null,
                             var audioUrl : String? = null,
                             var determiner : String? = null,
                             var locale : String? = null,
                             var alternateLocales : Array<String>? = null,
                             var siteName : String? = null,
                             var videoUrl : String? = null)
