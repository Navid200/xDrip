## xDrip Dexcom Intermittent Connectivity
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> xDrip intermittent Connectivity  
  
If you have no connectivity at all, [transmitter connectivity troubleshooting](./Connectivity-troubleshoot.md) may help.  
If you have connectivity and readings, but occasionally experience a disconnect, this page may help.  
  
xDrip needs to wake up and scan once every 5 minutes in order to maintain connectivity and provide consistent readings.  The rest of the time, xDrip is sleep, running in the background, to avoid draining the battery.  
New versions of Android have features to extend the phone battery run time by minimizing background activity.  
The phone manufacturers also include their methods for accomplishing the same.  
This behavior interferes with proper operation of xDrip.  As a result, when you first install xDrip on Android 10 or newer releases of Android, it is almost guaranteed it will have connectivity issues.  
  
#### 1. Please [update](./Updates.md) xDrip, at least, to the current stable release.  For G7, [see this](./Dexcom/G7.md).  
  
#### 2. Start from the recommended settings:
#### [G5](./G5-Recommended-Settings.md)
#### [G6/Dex1 or G7/1+](./G6-Recommended-Settings.md)  
  
#### 3. Disable any battery optimization/adaptation settings you can find under Android Settings &#8722;> Battery.  
For example, for Samsung, go to Settings &#8722;> Device care &#8722;> Battery, and disable "Power saving".  Then, tap on "Background usage limits", and disable "Put unused apps to sleep".  
  
#### 4. Look at Bluetooth Battery Optimization [here](./Dexcom-Basics.md).  Set it to not optimized or unrestricted.  
  
#### 5. Look for something similar for your phone and brand: [https://dontkillmyapp.com/xiaomi](https://dontkillmyapp.com/xiaomi)
<br/>  

If it keeps disconnecting, please use what follows as a method for tweaking your device to find the best settings.  
  
#### A. Enable "Special Pairing Workaround" even if you are not using a Samsung.
#### B. If you still get a disconnect more than once a week, try enabling "Avoid Scanning".  
  
If the problem persists, please [open a discussion](https://github.com/NightscoutFoundation/xDrip/discussions).  
  
