## Setting up Nightscout on Google Cloud
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Setting up Nightscout  
  
We will now log into our new Nightscout, installed on the Google Cloud virtual machine, and set it up.  Everything we do in this section is done on a regular web browser on our computer or mobile, and not in the virtual machine.   
  
Use a web browser and enter your hostname (from when you [set up](./FreeDNS.md) FreeDNS).  This will bring up your Nightscout.  It will ask you for a password.  Use the API secret you chose earlier when you [installed](./NS_Install) Nightscout.  
Check "Remember this device" checkbox to avoid having to re-enter it.  
On first time, Nightscout will ask you to set up a profile for sensitivity etc.  Either enter real values or just click save and then use close button on top right of the screen.  You may need to do this twice.  
  
<video width="400" controlsList="nodownload" src="./video/NS_Setup.mp4" controls>  
</video>  
<br/>    
<br/>  
  
---  
  
#### **Setting up xDrip to upload to your Nightscout**  
  
This is what you need to do in xDrip on your master phone.  
Go to xDrip Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API).  
Enable at the top.  

Let's say the subdomain you chose, when you signed up for FreeDNS, and the domain you chose it in was <span style="color:green">justaname\.chickenkiller\.com</span>.  
If you've forgotten what it is, you can log into FreeDNS and go to subdomains.  
  
And let's say you set the API secret, while finalizing the Nightscout installation as explained previously, to <span style="color:orange">ThisIsMyPassword</span>.  
If you've forgotten the password, you can see it on the first line of the nsconfig file ([Variables](./NS_Variables)).  
  
Tap on Base URL.  Enter the following.  
https://<span style="color:orange">ThisIsMyPassword</span>@<span style="color:green">justaname\.chickenkiller\.com</span>/api/v1/  
  
That's it.  Your xDrip readings should now upload to your Nightscout on Google Cloud.  
<br/>  
  
---  
  
#### **QR code**
Alternatively, you can scan a QR code to automatically set up xDrip to upload to Nightscout.  
To do that, use "Edit variables in a browser" from the menu.  After following the instructions and accessing the server in a browser, click on "Click here for QR configuration code" at the bottom of the page.  Then, scan the QR code that appears with xDrip.  
  
However, please note that doing this will erase any existing URLs from xDrip.  Therefore, if you intend to upload to multiple Nightscout sites, please use the manual method explained above.  
  
