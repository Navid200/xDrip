---
layout: GCNS
title: "Malfunctioning Nightscout applications"
permalink: "/docs/Nightscout/Auth_Default_Roles.html"
---

## Malfunctioning Nightscout applications
[Google Cloud Nightscout](./GoogleCloud.md) >> Malfunctioning applications  
  
There is a Nightscout variable titled AUTH_DEFAULT_ROLES.  The default value for this variable is "readable".  However, in our Google Cloud Nightscout setup, we have set this variable to "denied" be default.  Let's see why we have done that, and the consequences and options.  
  
#### **Security**
There is absolutely no security reason for setting this variable to denied.  That is not why we have set the variable to denied.  Please continue to read.  
  
#### **Privacy**
If you set the variable to readable, anyone who has your hostname will be able to view your readings with no need to a password or a token.  
You should not be ashamed of having diabetes.  But, you should know that there are insurance companies that will raise your premiums, for life insurance, if they know you have diabetes.  The solution is not to hide the fact that you have diabetes.  But, why would you want to advertise it?  
There may be other reasons one may wish to keep their readings confidential.  
Nevertheless, this is also not why we have set the variable to denied.  Please continue to read.  
  
#### **Traffic**
If you give read permission to the world, by setting this variable to readable instead of denied, crawlers will be able to create more output traffic from your server.  This could increase the cost of your Nightsdcout on Google Cloud.  
This is why we have set the variable to denied by default.  
  
By setting the variable to denied, I have been able to limit my cost to $0.01 Canadian for the month of December.  The reason for the 1 cent cost is crawlers in China.  
It may seem illogical that after closing permission to the world, crawlers could still cause traffic.  When anyone attempts to log in, Nightscout asks for a token or password.  This request is an output traffic.  The bandwidth would be much higher if the permission was open.  
  
#### **Can I change the setting to "readable"?**
Yes, you definitely can.  Doing that will make the settings the same as what most people had their Nightscout settings on Heroku.  But, Heroku output traffic was/is not limited.  
Changing the variable to "readable" may increase the cost of your Nightsocut.    
  
#### **Malfunctioning apps**
There are apps that will not function with the permission set to denied.  An example is AutotuneWeb.  
You can change the setting to readable to allow such an app to function.  However, your Nightscout cost may increase then.  
A better solution would be to contact the app developer and ask them to modify it such that it can use a token to read.  Then, the app will be able to function without causing the Nightscout traffic to unnecessarily increase.  
  
