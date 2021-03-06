![logo](https://github.com/lukasroberts/AndroidLinkView/blob/main/images/LinkViewLogo.png?raw=true)
![main_workflow_badge](https://github.com/lukasroberts/AndroidLinkView/actions/workflows/ci.yml/badge.svg) [![jitpack_badge](https://jitpack.io/v/lukasroberts/AndroidLinkView.svg)](https://jitpack.io/#lukasroberts/AndroidLinkView)

A Jetpack Compose Library for Rich URL Previews on Android.

## Usage

To include the library, add

`allprojects { repositories { maven { url 'https://jitpack.io' } } }`

To the where you include your repositories (either in settings.gradle, the main gradle file for the project, or per project)

Then add the dependency itself

`implementation 'com.github.lukasroberts:AndroidLinkView:1.0.1'`

The library itself is split into two parts, parsing, and previewing parsed metadata.

## Parsing Metadata

To parse metadata, you must use an implementation of the `IOpenGraphMetaDataProvider` class. The
default class for this is `OpenGraphMetaDataProvider`.

It's basic usage is as follows:

`val openGraphMetaDataProvider = OpenGraphMetaDataProvider()
val facebookResult = openGraphMetaDataProvider.startFetchingMetadataAsync(URL("https://facebook.com"))`

The above uses the suspend function, however a synchronous implementation also exists in the form

`startFetchingMetadata(link: URL)`

## Previewing Parsed Metadata

Once you have a `OpenGraphMetaData` instance, you can use it to render a preview. Currently one
preview exists, however it takes a `CardLinkPreviewProperties` builder that will let you customise
it in a large number of ways. See the screenshot below for what you can do with the current preview
renderer.

<img src="https://github.com/lukasroberts/AndroidLinkView/blob/main/images/LinkPreviewRenders.png" width="300" height="600">

## Sample Project

There is a sample project located under the SampleProject directory in this repository. This will
show you the basic usage of this library within Jetpack Compose.

## Notes:

1. This library does not support downloading images or displaying them. However, we expose an Image
   Painter and an array of ImageUri's that are picked up via parsing (If we were able to grab it)
   for you to do what you wish with based on whichever image library you are using. We have shown an
   example of how to do this in the sample project with a popular image library named
   Coil (https://coil-kt.github.io/coil/). This also shows how to cache the image in combination
   with this library, if that is something that you wish to do.
2. This library does not support the caching of metadata. Caching is up to the consumer of the
   library, and can be done in many ways. It is up to you how you wish to cache the metadata.
3. If you would like to create previews for HTTP requests then simply
   add: `android:usesCleartextTraffic="true"` to your manifest file. We do not add this to the
   library, since it is insecure and HTTPS should be the standard going forward. However we do try
   to handle redirects in the case that a site has one back to a HTTPS site.
