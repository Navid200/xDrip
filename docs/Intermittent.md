## xDrip Dexcom Intermittent Connectivity
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> xDrip intermittent Connectivity  
  
If you have no connectivity at all, [transmitter connectivity troubleshooting](./Connectivity-troubleshoot.md) may help. This page may help if you have connectivity and readings, but occasionally experience a disconnect.  
  
xDrip needs to wake up and scan every 5 minutes to maintain connectivity and provide consistent readings. The rest of the time, xDrip is in sleep mode, running in the background to conserve battery life. New versions of Android have features that extend phone battery life by minimizing background activity, and phone manufacturers also have their own methods for achieving this. Unfortunately, these behaviors can interfere with xDrip’s proper operation. As a result, when you first install xDrip on Android 10 or newer versions, connectivity issues are almost guaranteed.  
  
1. Ensure you are using at least the current stable release. [Update](./Updates.md) if necessary.  For G7, see [this](./Dexcom/G7.md).  
  
2. Start with the [recommended settings](./G6-Recommended-Settings.md).  
  
3. Look for similar settings for your phone brand: [https://dontkillmyapp.com/xiaomi](https://dontkillmyapp.com/xiaomi).  
<br/>  
  
If xDrip continues to disconnect, try the following adjustments.  
  
* Enable "Special Pairing Workaround".   This may help even if you are not using a Samsung device.  
  
* If you still experience disconnections, try `Avoid Scanning`.  
  
* If you are using a Samsung device, try `Trust Auto-Connect` on the [Bluetooth settings page](./images/ble-recommended-stngs.png).  
    
If the problem persists, please [open a discussion](https://github.com/NightscoutFoundation/xDrip/discussions) for further assistance.  
  
