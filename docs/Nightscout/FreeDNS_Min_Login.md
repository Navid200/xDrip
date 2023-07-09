---
layout: GCNS
permalink: "/docs/Nightscout/FreeDNS_Min_Login.html"
---

## FreeDNS login frequency
[Google Cloud Nightscout](./GoogleCloud.md) >> FreeDNS login requirement  
  
If you don't login to your FreeDNS account for 5.5 months, you will receive an email from them reminding you that you need to log in.  
Please [log into your FreeDNS account](./FreeDNS_Login.md).  
  
If you don't log into your account for 6 months, your account will become dormant and Nightscout will stop working.  
<br/>  
  
---  
  
#### **FreeDNS has gone dormant**  
This is what the [status page](./Status.md) will show if your FreeDNS account has gone dormant.  
![](./images/HostnameProblem.png)  
But, please note that this is also what you will see when you just install Nightscout for the first time before completing phase 2.  
This page should only be followed if you have had a fully functional Nightscout and now, it has deactivated.  
  
[Log into your FreeDNS account](./FreeDNS_Login.md).  You will see this:  
![](./images/DormantFreeDNS.png)  
  
Click on "proceed here".  You will see this:  
![](./images/AreYouStillUsingThisAccount.png)  
  
Click on "Extend your account".  
  
Access the subdomains page to confirm that your hostname is active again.  
[Restart the server](./Restart.md).  
  
If the hostname is active and you restart the server, the hostname will be mapped to the ip address, and Nightscout will resume operation again.  
  