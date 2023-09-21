---
layout: GCNS
---

## Malfunctioning Nightscout applications
[Google Cloud Nightscout](./GoogleCloud.md) >> Malfunctioning applications  
  
There is a Nightscout variable titled AUTH_DEFAULT_ROLES.  We have set this variable to "denied" by default.  See [why](./Why_Denied_Auth.md).  
  
#### **Malfunctioning apps**
There are apps that will not function with the permission set to denied.  An example is AutotuneWeb.  
You can change the setting to readable to allow such an app to function.  However, your Nightscout cost may increase then.  
A better solution would be to contact the app developer and ask them to modify it such that it can use a token to read.  Then, the app will be able to function without causing the Nightscout traffic to unnecessarily increase.  
  
#### **How to change the setting to "readable"?**
[Edit your variables](./NS_Variables.md).  
Find the variable and change the value from denied to readable.    

Preferably, after you are done using the malfunctioning app, change the variable back to denied.  

  
