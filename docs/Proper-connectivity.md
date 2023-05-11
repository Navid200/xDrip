## Proper Connectivity  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Proper Connectivity  
  
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
There is a disconnect if either of the above conditions is not satisfied.  
There is a disconnect if you see "Deep sleeping errors" or "Checking auth errors" on the status page as shown below.  
![](./Dexcom/images/DeepSleepingErrors.png)  
![](./Dexcom/images/CheckingAuthErrors.png)  
  
If there is a connectivity issue, you need to resolve it before issuing commands (start, stop, calibrate, hard reset, ...).  
  
