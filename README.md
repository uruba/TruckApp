# TruckApp – A TruckersMP companion app

[![GitHub release](https://img.shields.io/github/release/uruba/TruckApp.svg)](https://github.com/uruba/TruckApp/releases)
[![Build Status](https://travis-ci.org/uruba/TruckApp.svg)](https://travis-ci.org/uruba/TruckApp)
[![Coverage Status](https://coveralls.io/repos/github/uruba/TruckApp/badge.svg?branch=master)](https://coveralls.io/github/uruba/TruckApp?branch=master)

This will be a companion app for ~~ETS2MP~~ [**TruckersMP**](http://truckersmp.com/) players, compatible with Android devices. **Even though it's just a preview, you can get the current version directly on [Google Play](https://play.google.com/store/apps/details?id=cz.uruba.ets2mpcompanion), where you may also [opt in to the beta programme](https://play.google.com/apps/testing/cz.uruba.ets2mpcompanion) should you want to test the latest and the greatest, bugs and rough shape of the new features notwithstanding.**

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=cz.uruba.ets2mpcompanion&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge-border.png" width="180" /></a>
</p>

Or maybe you're looking for the [Windows 10 Mobile version](https://github.com/uruba/ETS2MP-Companion-Windows) (which is even more barebones right now, but that's going to change).
<p align="center">
  <a href="https://www.microsoft.com/store/apps/9nblggh4qsf0?ocid=badge"><img src="https://assets.windowsphone.com/d86ab9b4-2f3d-4a94-92f8-1598073e7343/English_Get_it_Win_10_InvariantCulture_Default.png" srcset="https://assets.windowsphone.com/5d2bd562-d242-4538-85f4-857d6457404b/English_Get_it_Win_10_InvariantCulture_Default.png 2x"  alt="Get it on Windows 10" /></a>
</p>

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
