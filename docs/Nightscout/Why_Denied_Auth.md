---
title: "Nightscout Variable 'AUTH_DEFAULT_ROLES' Default Setting"
description: "Explanation of why 'AUTH_DEFAULT_ROLES' is set to denied by default in Google Cloud Nightscout. Covers security reasons and when a change may be required."
layout: GCNS
---

# Why AUTH_DEFAULT_ROLES is set to "Denied"
[Google Cloud Nightscout](./GoogleCloud.md) >> AUTH_DEFAULT_ROLES set to denied  
  
The Nightscout variable titled **AUTH_DEFAULT_ROLES** is set to "denied" by default. Here’s why:  
  
#### **Security**
The default "denied" setting is not due to any specific security vulnerabilities with Google Cloud. Please read on for more details.  
  
#### **Privacy**
Setting the variable to "readable" would allow anyone with your hostname to view your readings without requiring a password or token. While there is no need to feel ashamed of having diabetes, it’s important to consider potential consequences. For instance, insurance companies might raise life insurance premiums if they are aware of your condition. Although concealing diabetes is not the solution, there are valid reasons to avoid publicly broadcasting it.  
  
#### **Can I change the setting to "readable"?**
Yes, you can [change the setting to "readable" if needed](./Auth_Default_Roles.md).  However, it's advisable to revert the setting to "denied" when possible to maintain privacy.  
  
