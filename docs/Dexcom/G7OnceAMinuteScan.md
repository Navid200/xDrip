## G7 Once a minute scan mode  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
A G7 (or One+) device transmits once every 5 minutes.  
The communication between a G7 and an app is a two-way communication.  There is an acknowledgement mechanism involved.  
If a G7 misses three consecutive acknowledgements (15 minutes), it switches to once a minute transmission (rapid reconnect) in order to help to reconnect faster, and this will continue for 12 hours or until the app successfully reconnects.  
  
xDrip scans once every 5 minutes on a regular basis.  However, when there is a disconnect, xDrip scans more frequently if `Settings` &#8722;> `Less common settings` &#8722;> `Aggressive service restarts` is enabled.  This has existed in xDrip since 2016 and is enabled by default.  You should never disable it.  
  
xDrip as is reconnects, to G7, rapidly when you return to your phone after a 15 minute or more away time.  
<br/>  
  
---  
#### **Verification**  
If you like to verify this for yourself, you can install this [scanner](https://play.google.com/store/apps/details?id=com.macdom.ble.blescanner&pcampaignid=web_share) for the duration of the test.  

Look at the list of connected or previously connected devices on your phone.  Identify the one starting with DXCM or DX02.  
Monitor the scanner until you see the same device name.  It may take up to 5 minutes.  It will become highlighted once every 5 minutes for a few seconds.  This is when the transmitter is transmitting.  Pay attention and take a note of the exact time it becomes highlighted next time.  
  
Now, put your phone in the microwave.  
Continue to monitor the scanner.  You will see that the transmitter continues to transmit once every 5 minutes for 3 more times.  After that, it will transmit every minute.  
When that happens, remove your phone from the microwave.  You will see that xDrip reconnects and shows readings the next time the transmitter transmits even if it is not on the original once-every-5-minute grid.  

  
