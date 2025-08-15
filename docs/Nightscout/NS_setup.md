---
layout: GCNS
title: "Setting Up Google Cloud Nightscout"
description: "Log into your Nightscout site on Google Cloud via FreeDNS, configure API secret, and enable xDrip uploads using QR or REST API."
---

# Setting up Google Cloud Nightscout
[Google Cloud Nightscout](./GoogleCloud.md) >> Setting up Nightscout  
  
We will now log into our new Nightscout, installed on the Google Cloud virtual machine, and set it up.  There are also video clips (silent) on this page that may be helpful if the instructions are not clear.  
You will need 5-10 minutes to complete this.  
  
Use a web browser and enter your hostname (from when you [set up](./FreeDNS.md) FreeDNS).  This will bring up your Nightscout.  It will ask you for a password.  Use the API secret you chose earlier when you [installed](./NS_Install.md) Nightscout.  
Check "Remember this device" checkbox to avoid having to re-enter it.  
On first time, Nightscout will ask you to set up a profile for sensitivity etc.  Either enter real values or just click save and then use close button on top right of the screen.  You may need to do this twice.  
  
<video width="400" controlsList="nodownload" src="./video/NS_Setup.mp4" controls>  
</video>  
<br/>    
<br/>  
<br/>  
  
---  
  
#### **Uploading to Nightscout**  
A continuous glucose monitor (CGM) may allow you to upload to Nightscout.  If you don't know how to set up your CGM to do that, please ask in a Nightscout support group.  This is an example: [https://www.facebook.com/groups/cgminthecloud](https://www.facebook.com/groups/cgminthecloud).  
<br/>  
  
---  
  
#### **Only if xDrip is your CGM**  
xDrip can easily upload its readings to Nightscout.  
If you use xDrip as your CGM, independently or in conjunction with another app, and want to make it your uploader, please use the following instructions.  
  
If you need help using xDrip as a CGM, please post in [xDrip discussions](https://github.com/NightscoutFoundation/xDrip/discussions) and ask for help.  
  
**QR code**  
You can scan a QR code to automatically set up xDrip to upload to Nightscout.  
To do that, go to the xDrip setup submenu.  Select "QR code to make xDrip master" on the submenu.  Then, scan the QR code that appears with xDrip autoconfigure (in xDrip, under Settings &#8722;> Auto configure).  
  
However, please note that doing this will erase any existing URLs from xDrip.  Therefore, if you intend to upload to multiple Nightscout sites, and you have already set up one of them, please use the manual method explained below.  
<br/>  
  
**Manual**  
Go to xDrip Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API).  
Enable at the top.  

Let's say the subdomain you chose, when you signed up for FreeDNS, and the domain you chose it in was <span style="color:green">justaname\.chickenkiller\.com</span>.  
If you've forgotten what the hostname is, follow these [instructions](./Hostname.md).  
  
And let's say you set the API secret, while finalizing the Nightscout installation as explained previously, to <span style="color:orange">ThisIsMyPassword</span>.  
If you've forgotten the password (API_SECRET), follow these [instructions](./API_SECRET.md).  
  
Tap on Base URL.  Enter the following.  
https://<span style="color:orange">ThisIsMyPassword</span>@<span style="color:green">justaname\.chickenkiller\.com</span>/api/v1/  
  
That's it.  Your xDrip readings should now upload to your Nightscout.  
  
  
