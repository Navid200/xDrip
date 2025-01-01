## G7 Rapid Reconnect  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
A G7 (or One+) device transmits data every 5 minutes under normal conditions.  
The following image shows an example:  
![](./images/RapidReconnect1.png)  
  
The communication between a G7 device and an app is two-way, involving an acknowledgment mechanism. If a G7 device misses three consecutive acknowledgments (15 minutes), it switches to a rapid reconnect mode. In this mode, it transmits every reading 4 additional times—once per minute—to facilitate faster reconnection. This mode continues for up to 12 hours or until the app successfully reconnects.  
The following image provides an example:  
![](./images/RapidReconnect2.png).  
  
As soon as a handshake takes place, the G7 stops the additional transmissions. There is no way for xDrip to determine which transmission is the original once-every-5-minute reading and which is not. Consequently, xDrip may pair and handshake on one of the additional transmissions. The issue in such a case is that when xDrip wakes up 5 minutes later, there will be no G7 transmission, as shown in the next figure.  
![](./images/RapidReconnect3.png)  
  
To expedite connectivity, pay attention to the Dex status page after approving the pair request and look for a note stating that xDrip is scanning.  If not, go to the classic status page and tap on `Restart Collector` to trigger scanning and go back to the Dex status page.  
After the red "Hunting Transmitter" note is removed from the Dex status page, you can leave the Dex status page.  Connectivity should be fine now.  

  
