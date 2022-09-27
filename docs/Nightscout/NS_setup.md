## Setting up Nightscout  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Setting up Nightscout  
  
We will now log into our new Nightscout, installed on the Google Cloud virtual machine, and set it up.    
  
Use a web browser and enter your hostname (from when you [registered](./noip_com) for noip.com).  This will bring up your Nightscout.  It will ask you for a password.  Use the API secret you chose earlier when you [installed](./NS_Install) Nightscout.  
Check "Remember this device" checkbox to avoid having to re-enter it.  
On first time, Nightscout will ask you to set up a profile for sensitivity etc.  Either enter real values or just click save and then use close button on top right of the screen.  You may need to do this twice.  
<br/>  
  
---  
  
#### **Setting up xDrip to upload to your Nightscout**  
  
This is what you need to do in xDrip on your master phone.  
Go to xDrip Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API).  
Enable at the top.  

Let's say the hostname you chose when you signed up for noip.com was <span style="color:blue">justausername.ddns.net</span>.  
If you've forgotten what it is, you can log into noip.com and go to dashboard.  The hostname is shown on that page under Free Dynamic DNS Hostname.  
  
And let's say you set the API secret, while finalizing the Nightscout installation as explained previously, to <span style="color:orange">ThisIsMyPassword</span>.  
If you've forgotten the password, you can see it on the first line of the nsconfig file ([Variables](./NS_Variables)).  
  
Tap on Base URL.  Enter the following.  
https://<span style="color:orange">ThisIsMyPassword</span>@justausername.ddns.net/api/v1/  
  
That's it.  Your xDrip readings should now upload to your Nightscout on Google Cloud.  
  
