## Connectivity troubleshooting
[xDrip](../README.md) >> [Features](Features_page.md) >> [Dexcom](./Dexcom_page.md) >> Connectivity troubleshooting  
  
Don't randomly tweak the xDrip settings hoping to get it going.  
Disable [engineering mode](./Engineering-Mode.md).  
  
When making changes to meet the following recommendations, be patient and allow a new reading to complete.  Communication with the transmitter happens only once every 5 minutes.  After you make a change, you are not going to see an impact, on the status page, until the next read cycle completes.  
  
If you are using G5 or G6 (or Dexcom One), please ensure your settings match the recommended [G5 settings](./G5-Recommended-Settings.md) or [G6 (or Dex1) settings](./G6-Recommended-Settings.md) respectively.  

Enable all xDrip notifications under Android Settings &#8722;> Apps & notifications &#8722;> Notifications &#8722;> xDrip+.  Otherwise, you may not get a pair request notification, which you will need to approve.  

If you are new to xDrip or Dexcom, have a look at the [general information](./Dexcom-Basics.md) to familiarize yourself with the interface and requirements and limitations.  

If there is another mobile device set to collect from the same transmitter (not a follower, but a master), you need to disconnect it.  

The G5/G6 status page gives you most of the information you need about how your system (xDrip, Dexcom transmitter and sensor) is doing.  
![](./images/system-status-pg.png)  

Is there a [disconnect](./Proper-connectivity.md)?  If yes, you need to resolve it before anything else.  Don't waste your time starting a sensor before you have connectivity.  
  
If there is a command in the queue on the status page, it should clear on the next read cycle (once every 5 minutes).  If it does not clear on the next read cycle, you have a command stuck in the queue.  In that case, [clear](./Clear-queue.md) the queue.  
  
If there is a disconnect, if you don't see "scanning" on the G5/G6 status page, you should tap on "Restart Collector" on the classic status page.  You may need to approve a pair request, which may come up any time up to 5 minutes after tapping on restart collector.  
  
If last connected, on the status page, consistently shows a value less than 5 minutes, yet, you see "Deep sleeping errors" or "Checking Auth errors" on the status page, go to the classic status page and tap on "Forget device".  Then, return to the G5/G6 status page and wait for a pair request to approve.  If you are using a version of Android older tha Android 10, you will not get a pair request.  But, you will hear a pairing notification.  The connectivity should be resolved after pairing.  
  
If there is still a disconnect, [trigger a pair request](./MissedPairRequest.md).  It should connect.  
  
If  there is no disconnect, and the status page shows "OK \*\* days" for "Sensor status", yet, you have no readings, it means that there is a session in progress and you are connected.  But, xDrip needs a start.  Please tap on start sensor.  You cna enter the same calibration code shown on the status page.  it doesn't really matter because the session has already started and the calibration code you enter now does not matter.  
  
If you are still having trouble, you can [ask for help](./Contact.md).  
