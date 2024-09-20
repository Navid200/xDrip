## Dexcom Connectivity troubleshooting
[xDrip](../README.md) >> [Features](Features_page.md) >> [Dexcom](./Dexcom_page.md) >> Dexcom Connectivity troubleshooting  
  
* Start from [recommended settings](./G6-Recommended-Settings.md).  
  
* Disable [engineering mode](./Engineering-Mode.md).  
  
* Allow time for changes to take effect.  Communication with the transmitter occurs every 5 minutes.  After making changes, wait for a new reading cycle to complete before assessing the impact on the status page.  
  
* For G6 or Dexcom One, ensure the transmitter is [activated](./Dexcom/NewG6TX_Activation.md).  

* For G7 or One+, follow [these steps](./Dexcom/G7.md).  
  
* Go to `Android Settings` &#8722;> `Apps & notifications` &#8722;> `Notifications` &#8722;> `xDrip+` and enable notifications and also disable DND.  This is crucial for receiving pair request notifications on Android 10 or newer.  
  
* Ensure no other mobile device is set to collect data from the same transmitter. If another device is connected as a master, disconnect it.  
  
* Check the Dex Status Page, which provides important information about the system’s performance.  
![](./images/system-status-pg.png)  
<br/>  
  
* If there is a command in the queue, it should clear in the next read cycle (every 5 minutes). If not, [clear the queue](./Clear-queue.md).  
  
* If the status page shows “Deep sleeping errors” or “Checking Auth errors” and the last connected time is consistently less than 5 minutes, tap “Forget Device” on the classic status page.  For G7 or One+, forget all devices starting with DXCM, DX01 or DX02 in your Bluetooth settings.  
Return to the Dex status page and approve a new pair request. For Android versions older than 10, you will not receive a pair request but will hear a pairing notification.  
  
* If there is still a disconnect, [trigger a pair request](./MissedPairRequest.md).  It should connect.  
  
* If  there is no disconnect, and the status page shows "OK \*\* days" for sensor status, but you have no readings, ensure the xDrip session is started.  Tap “Start Sensor” for G6/Dexcom One; you can enter the same calibration code shown on the status page. The calibration code entered now does not affect the already started session on G6.  G7 will start automatically.  
  
* If there is still a disconnect, [disable collection](./Stop-xDrip.md) in xDrip and instasll Dexcom app and follow its instructions to start.  If it does not work, you can contact Dexcom and ask for advice.  If it does work with Dexcom app, please [report it to us](./Contact.md).  
  
* If your system is functioning, but occasionally disconnects and requires action to reconnect, refer to the additional troubleshooting steps for [intermittent connectivity issues](./Intermittent.md).  
  
If you’re still experiencing trouble, don’t hesitate to [ask for help](./Contact.md).  
  
  
