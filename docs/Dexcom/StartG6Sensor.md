## Stop or start a G6 or Dexcom One sensor
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Stop or start G6/Dex1 Sensor  
  
If you are starting a new transmitter, please follow the instructions [here](../Starting-G6.md).  
But, if the transmitter has been used before and you are only starting a sensor on it, this guide is for you.  
<br/>  

---  

#### **Transmitter connectivity**  
You should never stop or start a sensor if there is a transmitter connectivity issue.  If there is a [connectivity issue](../Proper-connectivity.md), you need to [resolve](../Connectivity-troubleshoot.md) it before proceeding.  
<br/>  
  
---  
  
#### **Stop the previous sensor**    
Before starting a sensor, you need to stop the previous one.  
When you stop a sensor, you are stoping the session that is in progress on the transmitter.  The transmitter and xDrip only communicate for a few seconds once every 5 minutes.  In the meantime, the stop command stays in the queue and can be seen on the status page.  
As long as you can see the stop command in the queue on the status page, the transmitter has not received it and the session has not stopped.  

If you start a new sensor before the current session has successfully stopped, it is possible that the transmitter will never know that you have inserted a new sensor and will stop when the old sensor was going to stop.  Therefore, you should stop the previous session and see the queue on the status page clear before proceeding.  
<br/>  

---  

#### **Keep the transmitter out for 10 minutes**  
The transmitter has an anti-restart mechanism.  The transmitter can detect that it is not attached to an inserted sensor.  After you stop a session, if the transmitter detects that it is not connected for two consecutive readings, it concludes that you will be using a new sensor next.   
But, if after the session is stopped, the transmitter detects that it is disconnected for only one reading or not at all, it will conclude that a restart is being attempted if it receives a start command, and the start attempt will fail even if you are using a brand new sensor.  
Therefore, to avoid triggering a false restart attempt, always keep the transmitter out for at least 10 minutes (2 read cycles) after a successful stop.  
<br/>  

---  

#### **Start the new sensor**  
Only after the previous session has stopped, and you have confirmation by a clear transmitter queue on the status page, and after having kept the transmitter detached from your body for 10 minutes should you proceed with snapping in the transmitter and starting sensor.  
