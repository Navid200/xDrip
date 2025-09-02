---
layout: GCNS
title: "Nightscout AUTH_DEFAULT_ROLES: Fixing Malfunctions"
description: "Learn how Nightscout’s AUTH_DEFAULT_ROLES setting can prevent third-party apps (e.g. AutotuneWeb) from working—change 'denied' to 'readable' temporarily and revert after use."
---

# Malfunctioning Nightscout applications
[Google Cloud Nightscout](./GoogleCloud.md) >> Malfunctioning applications  
  
There is a Nightscout variable titled AUTH_DEFAULT_ROLES. By default, we have set this variable to "denied." Here’s [why](./Why_Denied_Auth.md).  
  
#### **Malfunctioning apps**
Some apps, such as AutotuneWeb, will not function if the permission is set to "denied." You can change the setting to "readable" to allow such an app to function, but this may increase your Nightscout costs. A better solution would be to contact the app developer and request that they modify the app to use a token for reading. This way, the app can operate without unnecessarily increasing Nightscout traffic.  
  
#### **How to change the setting to "readable"?**
[Edit your variables](./NS_Variables.md).  
Locate the `AUTH_DEFAULT_ROLES` variable and change its value from "denied" to "readable."  
  
Preferably, after you have finished using the malfunctioning app, change the variable back to "denied."  
  
  
