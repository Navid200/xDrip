---
title: "Starting and Stopping a Dexcom G6 Sensor in xDrip"
description: "Learn the correct way to start or stop a Dexcom G6 sensor in xDrip. This guide explains the transmitter queue, timing delays, and why commands may not take effect immediately, helping you avoid common mistakes."
---

# Stop or start a G6 or Dexcom One sensor
[xDrip](../../) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Stop or start G6/Dex1 Sensor  
  
If you are starting a new transmitter, please follow the instructions [here](../Starting-G6.md).  
However, if the transmitter has been used before and you are only starting a sensor on it, this guide is for you.  
<br/>  

---  

#### **Transmitter connectivity**  
Never stop or start a sensor if there is a transmitter connectivity issue. [Resolve](../Connectivity-troubleshoot.md) any connectivity [issues](../Proper-connectivity.md) before proceeding.  
<br/>  
  
---  
  
#### **Stop the previous sensor**    
Before starting a new sensor, you need to stop the previous one.  When you stop a sensor, you are stopping the current session on the transmitter. The transmitter and xDrip communicate only for a few seconds every five minutes. In the meantime, the stop command remains in the queue and can be seen on the status page.  

As long as the stop command is in the queue on the status page, the transmitter has not received it, and the session has not stopped.  
  
If you start a new sensor before the current session has successfully stopped, the transmitter may not recognize the new sensor. In this case, it might stop when the old sensor was due to stop. Therefore, you should stop the previous session and confirm the queue on the status page is clear before proceeding.  
<br/>  
  
---  

#### **Keep the transmitter out for 10 minutes**  
The transmitter has an anti-restart mechanism and can detect when it is not attached to an inserted sensor. After you stop a session, if the transmitter detects that it’s disconnected for two consecutive readings, it assumes you’ll be using a new sensor next.  
     
However, if the transmitter detects disconnection for only one reading or not at all after the session is stopped, it will interpret any start command as an attempted restart, and the start will fail, even if you're using a new sensor. To avoid triggering a false restart attempt, keep the transmitter out for at least 10 minutes (two reading cycles) after a successful stop.   
<br/>  

---  

#### **Start the new sensor**  
Only after the previous session has stopped, you have confirmed a clear transmitter queue on the status page, and you have kept the transmitter out for 10 minutes should you proceed with snapping in the transmitter and starting the sensor.    
<br/>  
  
---  

#### **What if you made a mistake?**  
If you start a new sensor and see readings before a 2-hour warm-up, check the status page. If the Sensor Status shows anything other than "0," it means you started too soon, and the transmitter still thinks it's on the previous sensor. Follow [the restart instructions here](../Restart-G6-sensor.md) as soon as possible to resolve this.   

  
