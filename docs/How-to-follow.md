## How to follow  
  
There are several options for following someone's readings.  
<br/>

### 1. xDrip Sync Follower  
This is easy to set up.  But, it relies on Google.  
On master, go to Settings &#8722;> xDrip+ Sync settings, and tap on "Be master for followers".  
**<span style="color:red">Disable "Allow follower actions".  Otherwise, the follower will be able to stop the sensor on master.</span>**  
Ensure "Disable all sync features", at the bottom of the page, is disabled.  
Tap on Settings &#8722;> Show Settings QR codes.  
Tap on xDrip plus security key settings only.  

On the follower, tap on Settings -> Auto configure.  
Scan the QR code shown on the master screen.  This will change all the settings, on the follower, to follow the master.  
Ensure Settings &#8722;> xDrip+ Sync settings &#8722;> Disable all sync features is disabled.  

In some cases, the scan does not work.  You can manually set up the follower:  
Under Settings &#8722;> Hardware Data Source, select xDrip+ Sync Follower.  
Then, go to Settings &#8722;> xDrip+ Sync Settings and tap on "Handset Group Security Sync Key".  Make sure the key matches the key on the same page on the master.  

On both master and follower ensure that Settings &#8722;> xDrip+ Sync Settings &#8722;> Disable all sync features is disabled.
<br/>

### 2. Nightscout Follower  
This requires Nightscout to be [set up](./Nightscout.md).  
After a Nightscout account has been created and set up, enter the required [settings](./Nightscout-Settings.md) so that the master starts uploading to Nightscout.  

On the follower, tap on Settings &#8722;> Hardware Data Source &#8722;> Nightscout Follower.  
Tap on Settings &#8722;> Nightscout Follow URL, and enter https<nolink>://userID.herokuapp.com, where userID is the master Nightscout user ID.  
<br/>

### 3. Dex Share Follower
This is an option if the master must be a Dexcom app, for example, if the master is an iPhone.  

On the follower, tap on Settings &#8722;> Hardware Data Source &#8722;> Dex Share Follower.  
Tap on Settings &#8722;> Share Username, to enter username.  
Tap on Settings &#8722;> Share Password, to enter the Dex share password.  
<br/>  
  
### 4. Build your own Dexcom app  
If you use the [Build your own Dexcom app](https://docs.google.com/forms/d/e/1FAIpQLScD76G0Y-BlL4tZljaFkjlwuqhT83QlFM5v6ZEfO7gCU98iJQ/viewform?fbzx=2196386787609383750&fbclid=IwAR2aL8Cps1s6W8apUVK-gOqgGpA-McMPJj9Y8emf_P0-_gAsmJs6QwAY-o0) on the same phone as xDrip, you will not need internet access because you will be able to use local broadcast.  Instructions are [here](./DexcomAppxDrip.md).  
