## Comparison of follow schemes  
[xDrip](../README.md) >> [Features](./Features_page) >> [Follow](./Follow_page.md) >> Comparison of xDrip follow schemes  
  
### **[xDrip Sync Follower](./xDripSync.md)** (needs internet except Desert Sync)  
This is easy to set up.  It relies on Google.  
<br/>  
  
### **Nightscout Follower** (needs internet)  
This requires Nightscout to be [set up](./Nightscout_page.md).  After Nightscout has been set up, enter the required [settings](./Nightscout-Settings.md) in xDrip so that the master xDrip starts uploading to Nightscout.  

On the follower, tap on Settings &#8722;> Hardware Data Source &#8722;> Nightscout Follower.  
Tap on Settings &#8722;> Nightscout Follow URL, and enter https<nolink>://Token@hostname, where Token is generated on the Nighscout admin page and hostname is the web address to your Nightscout.  
<br/>  
<br/>  
  
### **Dex Share Follower** (needs internet)  
This is an option if the master must be a Dexcom app.  
  
On the follower, tap on Settings &#8722;> Hardware Data Source &#8722;> Dex Share Follower.  
<br/>  
  
### **[Follow](./DexcomAppxDrip.md) Build your own Dexcom app**  
If you use the Build your own Dexcom app on the same phone as xDrip, you will not need internet access because you will be able to use local broadcast.  
<br/>  
  
### **[Companion App](./Follow/CompanionApp.md)**  
If you use CamAPS or the official Dexcom app as the collector, you can use xDrip on the same phone with no internet access.  
This option lacks back-fill capability.  There is a workaround for that, which may work for some phones with Android 12 or newer.  
<br/>  
  
### **[Web Follower](./Libre3_Follow.md)** (needs internet)  
If you use Libre 3, you can use this option to have readings in xDrip.  
  
