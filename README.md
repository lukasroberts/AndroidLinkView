# Android Link View
A Jetpack Compose Library for Rich URL Previews on Android.

This library does not support downloading images or displaying them. However, we expose 
an ImageView and an ImageUri (If we were able to grab it) for you to do what you wish with
based on whichever image library you are using. We have shown an example of how to do this
in the sample project with one of the most popular image libraries Glide. 
This also shows how to cache the image in combination with this library, if that is something that you wish to do.

If you would like to create previews for HTTP requests then simply add:

android:usesCleartextTraffic="true"

to your manifest file. We do not add this to the library, since it is insecure and HTTPS should be the standard going forward.

We also handle redirects in the case that a site has one back to a HTTPS site.

Note that this also works without Jetpack Compose with the "old" way. Those views are built with Constraint Layout for efficiency.
