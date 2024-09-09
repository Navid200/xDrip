---
layout: GCNS
---

## Why AUTH_DEFAULT_ROLES is set to "Denied"
[Google Cloud Nightscout](./GoogleCloud.md) >> AUTH_DEFAULT_ROLES set to denied  
  
There is a Nightscout variable titled `AUTH_DEFAULT_ROLES`.  We have set this variable to "denied" by default. Here’s why:  
  
#### **Security**
The default "denied" setting is not due to any security vulnerabilities with Google Cloud. Please continue reading for further details.  
  
#### **Privacy**
Setting the variable to "readable" would allow anyone with your hostname to view your readings without needing a password or token.  While it's important not to feel ashamed of having diabetes, it’s also crucial to consider that insurance companies might raise premiums for life insurance if they are aware of your condition. While hiding the fact of having diabetes is not the solution, there may be reasons for not wanting to publicly advertise it.  However, privacy concerns are also not the primary reason for the default "denied" setting.  
  
#### **Traffic**
Granting read permission to everyone by setting this variable to "readable" could lead to increased output traffic from your server, potentially raising your Nightscout costs on Google Cloud.  This is the main reason we have set the variable to "denied" by default.  
  
#### **Can I change the setting to "readable"?**
Yes, you can [change the setting to "readable" if needed](./Auth_Default_Roles.md).  However, it's advisable to revert the setting to "denied" when possible to manage costs and maintain control.  
  
