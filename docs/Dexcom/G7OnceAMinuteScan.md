## G7 Rapid Reconnect  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
A G7 (or One+) device transmits data every 5 minutes under normal conditions.  
The communication between a G7 and an app is two-way, involving an acknowledgment mechanism.  

If a G7 device misses three consecutive acknowledgments (15 minutes), it switches to a rapid reconnect mode, transmitting data once per minute to facilitate faster reconnection. This mode continues for up to 12 hours or until the app successfully reconnects.  

While in rapid reconnect mode, the G7 device transmits each reading five times. The following image provides an example:  
./images/RapidReconnect2.png.  
  
  
