## How to stop or start a G6 or Dexcom One sensor
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Stop or start G6/Dex1 Sensor  
  
If you are starting a new transmitter, please follow the instructions [here](./Starting-G6.md).  
But, if the transmitter has been used before and you are only starting a sensor on it, this guide is for you.  
<br/>  

---  

#### **Stop the previous sensor**    
Before starting a sensor, you need to stop the previous one.  
When you stop a sensor, you are stoping the session that is in progress on the transmitter.  The transmitter and xDrip only communicate for a few seconds once every 5 minutes.  In the meantime, the stop command stays in the queue and can be seen on the status page.  
As long as you can see the stop command on the status page, the transmitter has not received it and the session has not stopped.  

If you start the new sensor before the session has stopped, it is possible that the transmitter will never know that you have inserted a new sensor and will stop when the old sensor was going to stop.  Therefore, you should ensure to stop the previous session before proceeding.  
<br/>  

---  

#### **Keep the transmitter out for 10 minutes**  
The transmitter has an anti-restart mechanism.  The transmitter can detect that it is not attached to an inserted sensor.  After you stop a session, if the transmitter detects that it is not connected for two consecutive readings, it assumes that you will be using a new sensor next.   
But, if after the session is stopped, the transmitter detects that it is disconnected for only one reading, it will conclude that a restart is being attempted and the following start attempt will fail.  
To avoid triggering a restart attempt, always keep the transmitter out for at least 10 minutes (2 read cycles) after stopping a session.  
