## xDrip Dexcom Intermittent Connectivity
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> xDrip intermittent Connectivity  
  
If you have no connectivity at all, [transmitter connectivity troubleshooting](./Connectivity-troubleshoot.md) may help. This page is intended for situations where you have connectivity and readings but occasionally experience a disconnect.  
  
xDrip needs to wake up and scan every 5 minutes to maintain connectivity and provide consistent readings. When not actively scanning, xDrip runs in the background to conserve battery life.  Newer versions of Android include features to extend battery life by minimizing background activity, and manufacturers often implement their own methods for this. Unfortunately, these behaviors can interfere with xDrip’s operation. Therefore, when you first install xDrip on Android 10 or newer versions, connectivity issues are common.  
  
1. Ensure you are using at least the current stable release. [Update](./Updates.md) if necessary.  If using G7 or One+, see [this](./Dexcom/G7.md).  
  
2. Start with the [recommended settings](./G6-Recommended-Settings.md).  
  
3. Look for similar settings for your phone brand: [https://dontkillmyapp.com/xiaomi](https://dontkillmyapp.com/xiaomi).  
<br/>  
  
If xDrip continues to disconnect, try the following adjustments:  
  
* Enable "Special Pairing Workaround".   This may help even if you are not using a Samsung device.  
  
* If disconnections persist, try enabling `Avoid Scanning`.  
  
* For Samsung devices, try `Trust Auto-Connect` on the [Bluetooth settings page](./images/ble-recommended-stngs.png).  
    
If the problem persists, please [open a discussion](https://github.com/NightscoutFoundation/xDrip/discussions) for further assistance.  
  
