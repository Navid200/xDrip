---
layout: GCNS
---

## Restart Nightscout
[Google Cloud Nightscout](./GoogleCloud.md) >> Restart Nightscout   
  
To restart Nightscout, you can reboot the virtual machine.  
To do that, open a [terminal](./Terminal).  From the [menu](./Menu.md), select "Reboot server" and enter.  
  
The terminal will then show a [connection error](./ConnectionFailed.md) because it will be disconnected from the machine since it is rebooting.  
Wait a minute, and Nightscout will be back up and running again.  
<br/>  
  
---  

#### **IP address**  
Restarting the server will not change the IP address.  
If the virtual machine is stopped, after you restart it, its IP address will be different than what it was before when it was running.  But, this is not a problem.  You just need to be patient for about a minute for the new IP address to be mapped to the hostname.  
<br/>  

---  
  
#### **Note**  
You run Nightscout on a Google Cloud virtual machine that you created as a part of the original installation.  If you look under installation on the Google Cloud Nightscout [main page](./GoogleCloud.md), you will see the "Virtual Machine" heading link you must have followed during installation.  
  
Therefore, restarting the virtual machine will restart the server.  
  
