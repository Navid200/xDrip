---
title: "Why You Can’t Manually Stop a Dexcom G7 in xDrip"
description: "Understand why xDrip removes manual stop/start for the Dexcom G7: sessions auto-start and stopping it permanently disables the device, which continues transmitting anyway."
---

# Why you can’t manually stop or start a G7  
[xDrip](../../) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Why no start or stop for G7!  
  
A G7 (or One+) session starts automatically on the device as soon as you use the applicator to insert it. There is no need to manually start the session.  
  
With the G6 transmitter, you could use a single transmitter for up to 110 days, allowing up to 11 sensors to be used sequentially with it. When starting a new G6 sensor, you had to stop the previous session first because the transmitter could only run one session at a time. This is why a "stop" function was necessary for G6.  
  
In contrast, the G7 integrates both the sensor and transmitter into a single, disposable device. Once a session is finished, the device cannot be reused.  If you stop a G7 session, you will never be able to restart it.  Because of this, a manual stop function is unnecessary.  
  
You might think stopping a G7 would prevent Bluetooth interference with your next sensor.  However, just like the G6, stopping a session only ends the data processing—it does not turn off the transmitter. A G7 transmitter continues sending Bluetooth signals every five minutes for as long as the battery lasts (which can be over 100 days), even after the session ends. Stopping the session does not affect these transmissions.  
  
To prevent accidental stoppage, the ability to manually stop a G7 has been removed from xDrip. Removing this option ensures you won’t unintentionally end a session and permanently disable a functional sensor.  
  
#### **How to end a G7 session**  
You can remove a G7 that you no longer wish to use, even if it is still functional. However, if you do so while it is still active, xDrip may receive one last reading at a very low value as the sensor is pulled from the skin, which can skew your glucose history. To avoid this, [disable collection](../Stop-xDrip.md) in xDrip before peeling off the sensor.  
<br/>  
  
---  

[How to start a G7](./G7.md)  
[How to start a subsequent G7](./SubsequentG7.md)  
[G7 grace period](./G7_Grace.md)  
  
