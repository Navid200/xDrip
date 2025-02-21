## G7 Rapid Reconnect  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
A G7 (or One+) device transmits data every 5 minutes under normal conditions.  
The following image shows an example:  
![](./images/RapidReconnect1.png)  
<br/>  
  
The communication between a G7 device and an app is two-way, utilizing an acknowledgment mechanism. If a G7 device misses three consecutive acknowledgments (15 minutes), it switches to a rapid reconnect mode. In this mode, the device transmits each reading 4 additional times—once per minute—to facilitate faster reconnection. This mode continues for up to 12 hours or until the app successfully reconnects.  
The following image provides an example:  
![](./images/RapidReconnect2.png).  
<br/>  
  
As soon as a handshake occurs, the G7 stops the additional transmissions. However, xDrip cannot distinguish between the original once-every-5-minute reading and the additional transmissions. As a result, xDrip may pair and handshake with one of the additional transmissions. The issue with this scenario is that when xDrip wakes up 5 minutes later, no G7 transmission will occur, as illustrated in the next figure.  
![](./images/RapidReconnect3.png)  
<br/>  
  
To expedite connectivity, follow these steps:  
1- Monitor the Dex status page after approving the pairing request and check for a "Connect request" note or an indication that xDrip is scanning.  
2- If neither appears, navigate to the classic status page and tap `Restart Collector` to trigger scanning.  
3- Return to the Dex status page and wait until the red “Hunting Transmitter” note disappears.  
  
Once the red note is gone, you can leave the Dex status page. Connectivity should now be successfully established.  
  
