## Setting up Nightscout  
[xDrip](../../README.md) >> [xDrip & Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./NS_GoogleCloud) >> [Setting up Nightscout](./NS_setup)  
  
Use a web browser and enter your hostname (from when you registered for noip.com).  This will bring up your Nightscout.  It will ask you for a password.  Use the API secret you chose earlier.  
Check "Remember this device" checkbox to avoid having to re-enter it.  
On first time, Nightscout will ask you to set up a profile for sensitivity etc.  Either enter real values or just click save and then use close button on top right of the screen.  You may need to do this twice.  
<br/>  
  
#### **Setting up xDrip to upload to your Nightscout**  
  
This is what you need to do in xDrip on your master phone.  
Go to xDrip Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API).  
Enable at the top.  
Let's say the hostname you chose when you signed up for noip.com was hostname.  And let's say you set the API secret, while finalizing the installation as explained in the previous section, to API-Secret.   
Tap on Base URL.  Enter the following.  
https://API-Secret@hostname/api/v1/  
  
That's it.  Your xDrip readings should now upload to your Nightscout on Google Cloud.  
  
