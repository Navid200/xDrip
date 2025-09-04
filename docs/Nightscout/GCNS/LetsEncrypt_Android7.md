---
title: "Let's Encrypt Support for Android 7 Has Ended"
description: "Learn about the end of free support for Android 7 devices accessing Google Cloud Nightscout sites using Let's Encrypt certificates. Understand the SSL handshake errors and get guidance for continuing to access your Nightscout site."
layout: GCNS
---

# Let’s Encrypt Support for Android 7 Has Ended
[Google Cloud Nightscout](../GoogleCloud.md) >> Let's Encrypt and Android 7  

Google Cloud Nightscout uses Let’s Encrypt. Unfortunately, free support for Android 7 has ended, as described [here](https://community.letsencrypt.org/t/support-for-android-7-and-older-from-oct-2024/216446).  If you try to access a Google Cloud Nightscout site using Android 7 or older, it will fail. Devices running newer versions will continue to work fine.  
  
You may see the following error on your uploader:  
`javax.net.ssl.SSLHandshakeException: Java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.`  

In the case of a follower app or web browser, you may see a message like:  
`... can't open the page.`  
  
To access your site, use a device running Android 8 or newer.  
  
