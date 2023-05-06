## xDrip Dexcom Intermittent Connectivity
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> Intermittent Connectivity  
  
xDrip needs to wake up and scan once every 5 minutes in order to maintain connectivity and provide consistent readings.  
New versions of Android have features to extend the phone battery run time.  This is done by minimizing background activity.  
The phone manufacturers also include their methods for accomplishing the same.  
This behavior interferes with proper operation of xDrip.  As a result, when you first install xDrip on Android 10 or newer releases of Android, it is almost guaranteed it will have connectivity issues.  
  
It is hard to provide instructions for all available brands and models of handsets.  Please use what follows as a method for tweaking your device to find the best settings.  The following link is an example of tips for one particular brand of phone.  Search for something similar for your phone brand.  
[https://dontkillmyapp.com/xiaomi](https://dontkillmyapp.com/xiaomi)
  
#### 1. If you are using an xDrip release older than the current stable release, please [update](./Updates.md).
#### 2. Start from the recommended settings:
#### [G5](./G5-Recommended-Settings.md)
#### [G6 or Dex1](./G6-Recommended-Settings.md)
#### 3. Disable any battery optimization/adaptation settings you can find under Android Settings &#8722;> Battery.  
For example, for Samsung, go to Settings &#8722;> Device care &#8722;> Battery, and disable "Power saving".  Then, tap on "Background usage limits", and disable "Put unused apps to sleep".  
#### 4. Look at Bluetooth Battery Optimization [here](./Dexcom-Basics.md).  Set it to not optimized or unrestricted.
#### 5. If it keeps unpairing, try enabling "Special Pairing Workaround" even if you are not using a Samsung.
#### 6. If you still get a disconnect more than once a week, try disabling "Avoid Scanning". 
#### 7. If the problem persists, please [open a discussion](https://github.com/NightscoutFoundation/xDrip/discussions) (not an issue).
  
