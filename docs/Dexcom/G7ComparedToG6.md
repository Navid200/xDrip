---
title: "Dexcom G7 vs G6: key differences when using xDrip"
description: "Learn the key differences between Dexcom G6 and G7 when using xDrip, including sensor behavior, connection order, session handling, and data transmission."
---

# Dexcom G7 vs G6: key differences when using xDrip  
[xDrip](../../) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 vs G6: key differences when using xDrip  
  
Many Dexcom G6 users are now preparing to switch to G7. This page outlines the primary differences you can expect when using G7 with xDrip.  
  
**1- Initial reading stability**  
There are more fluctuations in readings, especially during the first day of a sensor. This is not related to xDrip.  
If you use AAPS, you can use one of the existing smoothing options in AAPS to address this.  
  
**2- Backfill capability**  
G6: Backfills up to 3 hours of data. To keep a full record on a G6 receiver, it must be turned on or kept in range at least once every 3 hours.  
G7: Backfills up to 24 hours of data. You only need to turn on a G7 receiver once a day for it to contain all your readings.  
  
**3- Session management (starting and stopping)**  
G6: A session must be manually started from an app, receiver, or pump.  
G7: The sensor session starts automatically upon insertion.  
Because the G7 handles its own activation, there is no "Start sensor" or "Stop sensor" option in xDrip.  
  
**4- Configuration settings**  
To use xDrip with the G7, you will need to scan the configuration QR code provided in the setup guide.  
  
**5- Connection order**  
G6: It is generally recommended to connect your pump before connecting xDrip.  
G7: If your pump connects directly to the G7, you should connect xDrip first, then connect the pump.  Similarly, if you have a G7 receiver, pair xDrip before pairing the receiver.  
  
**6- Transmission modes**  
G6: Transmits data once every 5 minutes.  
G7: Also transmits every 5 minutes but includes a "rapid reconnect" mode.  If the G7 loses connectivity for 15 consecutive minutes, it switches to transmitting once per minute to re-establish the link more quickly.  
  
**7- Sensor lifespan and grace period**  
G6: Runs for 10 days. Both the Dexcom app and xDrip report this clearly.  
G7: Runs for 10.5 days.  The Dexcom app reports this as 10 days with a 12-hour grace period.  xDrip reports the full 10.5 days as the standard lifetime.  It is your choice how to use those extra 12 hours.  
  
**8- Bluetooth troubleshooting**  
G6: Disconnecting is easy because the Bluetooth name is associated with the serial number.  There is a "Forget device" button on the classic status page to unpair easily.   
G7: The Bluetooth name is not associated with the serial number or pairing code.  Therefore, there is no "Forget device" option on the classic status page for G7.  Please refer to the specific [G7 troubleshooting guide](./G7_Troubleshooting.md) for connectivity issues.  
  
---  

[How to start a G7](./G7.md)  
[How to start a subsequent G7](./SubsequentG7.md)  
[G7 troubleshooting guide](./G7_Troubleshooting.md)  
[G7 grace period](./G7_Grace.md)  
