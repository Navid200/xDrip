## Proper Connectivity  
  
Go to G5/G6 status page.  
![](./images/system-status-pg.png)  
<br/>  

You have proper connectivity only if all the following three conditions are satisfied:
1. There is a line starting with "Last Connected:".  
2. The "Last Connected" value is less than 5 minutes as long as you stay near the phone.  
3. If you wait for consecutive read cycles (5 minutes each), the "Last Connected" value gradually increases rolling back to 0 just before reaching 5 minutes every cycle.  
<br/>  

You don't have proper connectivity if either of the above conditions is not satisfied.  

If there is a connectivity issue, you need to resolve it before issuing commands (start, stop, calibrate, hard reset, ...).  
