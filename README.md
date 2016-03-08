# ETS2MP-Companion

[![GitHub release](https://img.shields.io/github/release/uruba/ETS2MP-Companion.svg)](https://github.com/uruba/ETS2MP-Companion/releases)
[![Build Status](https://travis-ci.org/uruba/ETS2MP-Companion.svg)](https://travis-ci.org/uruba/ETS2MP-Companion)
[![Coverage Status](https://coveralls.io/repos/uruba/ETS2MP-Companion/badge.svg?branch=master&service=github)](https://coveralls.io/github/uruba/ETS2MP-Companion?branch=master)

This will be a companion app for ~~ETS2MP~~ [**TruckersMP**](http://truckersmp.com/) players, compatible with Android devices. **Even though it's just a preview, you can get the current version directly on [Google Play](https://play.google.com/store/apps/details?id=cz.uruba.ets2mpcompanion), where you may also [opt in to the beta programme](https://play.google.com/apps/testing/cz.uruba.ets2mpcompanion) should you want to test the latest and the greatest, bugs and rough shape of the new features notwithstanding.**

## Current state

It is still very early in the development, so there's not much to see here just yet. However, you are more than welcome to join the effort – if you so will. In that case message me at [uruba@outlook.com](mailto:uruba@outlook.com) and we can talk further. Or not. Pull requests are welcome either way.

## Get involved

*If you have any suggestion or want to report an issue, you can post in [the application's thread on the TruckersMP discussion board](https://forum.truckersmp.com/index.php?/topic/22715-unofficial-ets2mp-companion-android-application-concept/) or you can use the issue tracker here on GitHub.*


**At this stage of development, everything is up for a discussion – especially the feature set that the app is to contain. Every comment, suggestion or critique is therefore very welcome.**

## Additional libraries used in the project

* [Butter Knife](http://jakewharton.github.io/butterknife/) (*com.jakewharton:butterknife*) – for injecting view classes (to achieve a "less messy" code)
* [jsoup](http://jsoup.org/) (*org.jsoup:jsoup*) – for parsing the ets2c.com meetup site
* [Google Analytics for Android](https://developers.google.com/analytics/devguides/collection/android/v4/) (*com.google.android.gms:play-services-analytics*) – to observe application/user interaction metrics 
* [Espresso](https://google.github.io/android-testing-support-library/docs/espresso/index.html) (*com.android.support.test.espresso:espresso-core*) – for UI testing
* [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) (*com.squareup.okhttp3:mockwebserver*) – for testing against a mock server (to ensure immutable server response during certain testing scenarios)


## Early screenshots

<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/4870410/11044511/9b297cec-8722-11e5-8e87-46389026739c.png" alt="Server list screen"/>
</p>

<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/4870410/11044534/c05c6a74-8722-11e5-9581-7d6a0438e3f1.png" alt="Player meetups screen"/>
</p>
