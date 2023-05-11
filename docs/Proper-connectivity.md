## Proper Connectivity  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Proper Connectivity  
  
Dexcom is a sample and hold system.  Things update only once every 5 minutes and for the rest of the time, nothing happens.  
xDrip shows what the transmitter reports once every 5 minutes.  If you see a disconnect, and you change something, you have to wait at least for the next connection in 5 minutes before you see any updates due to the change you made.  This could take even longer.  For example, let's say you tap on stop sensor.  You can see the command goes into a queue shown on the status page.  On the next reading, the transmitter receives the request.  At that point, the transmitter still is not considering itself to have been stopped because it transmitted its status at the same time it received the request to stop.  If you wait 5 more minutes, you will see an acknowledgement from the transmitter on the status page that the transmitter has stopped now.  

Therefore, please don't look at the status page wondering why things make no sense!  Please be patient and allow the sample and hold system to operate and update.  
<br/>  
  
---  
  
#### **Connectivity**  
Go to G5/G6 status page.  
![](./images/system-status-pg.png)  
<br/>  
  
The connectivity is OK only if, looking at the system status page, all the following conditions are satisfied:
1. There is a line starting with "Last Connected:".  
2. The "Last Connected" value is less than 5 minutes as long as you stay near the phone.  
3. If you wait for consecutive read cycles (5 minutes each), the "Last Connected" value gradually increases rolling back to 0 just before reaching 5 minutes every cycle.  
4. You don't see messages like hunting transmitter, scanning error, waiting connect errors, power connect error, ...  
<br/>  
  
---  
  
#### **Disconnect**  
There is a disconnect if either of the conditions mentioned in the previous section is not satisfied.  
There is a disconnect if you see "Deep sleeping errors" or "Checking auth errors" on the status page as shown below.  
![](./Dexcom/images/DeepSleepingErrors.png)  
  
![](./Dexcom/images/CheckingAuthErrors.png)  
  
If there is a connectivity issue, you need to resolve it before issuing commands (start, stop, calibrate, hard reset, ...).  
  
