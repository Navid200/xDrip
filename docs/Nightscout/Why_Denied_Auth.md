---
layout: GCNS
---

## Why AUTH_DEFAULT_ROLES is set to denied?
[Google Cloud Nightscout](./GoogleCloud.md) >> AUTH_DEFAULT_ROLES set to denied  
  
There is a Nightscout variable titled AUTH_DEFAULT_ROLES.  We have set this variable to "denied" by default.  Let's see why we have done that.  
  
#### **Security**
There is no security vulnerability with Google Cloud.  That is not why we have set the variable to denied.  Please continue to read.  
  
#### **Privacy**
If you set the variable to readable, anyone who has your hostname will be able to view your readings with no need to a password or a token.  
You should not be ashamed of having diabetes.  But, you should know that there are insurance companies that will raise your premiums, for life insurance, if they know you have diabetes.  The solution is not to hide the fact that you have diabetes.  But, why would you want to advertise it?  
There may be other reasons one may wish to keep their readings confidential.  
Nevertheless, this is also not why we have set the variable to denied.  Please continue to read.  
  
#### **Traffic**
If you give read permission to the world, by setting this variable to readable instead of denied, crawlers will be able to create more output traffic from your server.  This could increase the cost of your Nightsdcout on Google Cloud.  
This is why we have set the variable to denied by default.  
  
#### **Can I change the setting to "readable"?**
Yes, [you definitely can](./Auth_Default_Roles.md).  
But, it's best to change it back if/when possible.  
  
