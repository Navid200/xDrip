## Applications fail
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >> Applications that fail  
  
There is a Nightscout variable titled AUTH_DEFAULT_ROLES.  The default value for this variable is readable.  However, we have set this variable to denied.  Let's see why.  
  
#### **Security**
There is absolutely no security reason for setting this variable to denied.  That is not why we have set the variable to denied.  Please continue to read.  
  
#### **Privacy**
If you set the varibale to readable, anyone who has your hostname will be able to view your readings.  You should not be ashamed of having diabetes.  But, you should know that there are insurance companies that will raise your premiums, for life insurance, if they know you have diabetes.  
The solution is not to hide the fact that you have diabetes.  But, why would you want to advertise it?  
  
#### **Bandwidth**
If you give read permission to the world, by setting this variable to readable instread of denied, crawlers will be able to create higher bandwidth output from your server.  This could increase the cost of your Nightsdcout on Google Cloud.  
By setting the variable to denied, I have been able to lower my cost to $0.01 Canadian for the month of December.  The reason for the 1 cent cost is crawlers in China.  
It may seem illogical that after closing permission to the world, crawlers could still cause traffic.  The bandwidth would be much higher if the permission was open.  
  
#### **Malfunctioning apps**
There are apps or features that will not function with the permissions set to denied.  An example is Autotune.  
You can change the setting to readable to allow such an app to function.  However, you should be aware your Nightscout cost may go up.  
A better solution would be to contact the app developer and ask them to modify it to be able to use a token.  
  
