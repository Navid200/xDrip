## xDrip Dexcom Intermittent Connectivity
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> xDrip intermittent Connectivity  
  
If you have no connectivity at all, [G6 troubleshooting](./Connectivity-troubleshoot.md) or [G7 troubleshooting](./Dexcom/G7_Troubleshooting.md) may help. This page is intended for cases where you experience intermittent disconnections despite having connectivity and readings.  
  
xDrip scans every five minutes to maintain connectivity and provide consistent readings.  While not actively scanning, it runs in the background to conserve battery life.  Newer versions of Android incorporate features to extend battery life by reducing background activity, and phone manufacturers often implement additional power-saving methods.  Unfortunately, these behaviors can interfere with xDrip’s operation.  As a result, initial connectivity issues are common when installing xDrip on Android 10 or newer.  
  
1. Ensure you are using at least the current stable release—[update](./Updates.md) if necessary.  
  
2. Start with the [recommended settings](./G6-Recommended-Settings.md).  
  
3. Check for similar settings for your phone brand: [https://dontkillmyapp.com/xiaomi](https://dontkillmyapp.com/xiaomi).  
<br/>  
  
If occasional disconnects continue, try the following adjustments:  
  
* Enable "Special Pairing Workaround"—this may help even if you are not using a Samsung device.  
  
* If disconnections persist, try enabling `Avoid Scanning`.  
  
* For Samsung devices, try enabling `Trust Auto-Connect` on the [Bluetooth settings page](./images/ble-recommended-stngs.png).  
    
If the problem persists, please [open a discussion](https://github.com/NightscoutFoundation/xDrip/discussions) for further assistance.  
  
