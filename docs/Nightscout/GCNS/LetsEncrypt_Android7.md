---
layout: GCNS
---

## Let’s Encrypt Support for Android 7 Has Ended
[Google Cloud Nightscout](./GoogleCloud.md) >> Let's Encrypt and Android 7  

Our setup uses Let’s Encrypt. Unfortunately, free support for Android 7 has ended, as described [here](https://community.letsencrypt.org/t/support-for-android-7-and-older-from-oct-2024/216446).  If you attempt to access the site with Android 7 or older, it will fail. Any other device will work fine.  

This is an error you may see on your uploader:  
`javax.net.ssl.SSLHandshakeException: Java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.`  
  
You will need to use a device running Android 8 or newer to access your site.  
  
