---
title: "Restart Nightscout on Google Cloud"
description: "Learn how to safely restart your Google Cloud Nightscout instance using the setup menu or SSH. Includes documentation and help for minimizing downtime and ensuring proper service recovery."
layout: GCNS
---

# Restart Nightscout
[Google Cloud Nightscout](./GoogleCloud.md) >> Restart Nightscout   
  
To restart Nightscout, reboot the virtual machine by following these steps:  
1- [Open a terminal](./Terminal).  
2- From the [main menu](./Menu.md), select `Reboot server` and confirm.  
  
**<span style="color:red">Connection Failed</span>**  
The terminal will display a [connection error](./ConnectionFailed.md) because it disconnects from the machine during the reboot process.  
This is expected behavior, and you do not need to click on **Troubleshoot**.  Simply wait about a minute, and Nightscout will be back up and running.   
<br/>  
  
---  

#### **IP address**  
Restarting the server does **not** change the IP address.  
However, if the virtual machine is stopped and then restarted, its IP address will be different from the previous one.  This is not an issue; you just need to wait approximately a minute for the new IP address to map to the hostname.  
<br/>  

---  
  
#### **Note**  
Nightscout runs on a Google Cloud virtual machine that was set up during the initial installation.  
As a result, restarting the virtual machine is equivalent to restarting the Nightscout server.  
  
