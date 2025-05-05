---
layout: GCNS
---

## Switch Network Service Tier    
[Google Cloud Nightscout](../GoogleCloud.md) >> Switch Network Service Tier  
<br/>  
  
By default, the Network Service Tier is set to Premium, which can be costly for outgoing traffic. Switching to Standard allows for free outgoing traffic up to 200GB per month. We have been using this setting without any negative impact on Nightscout’s performance.  
  
To change the network service tier of an active machine, you must stop it first.  
The video below demonstrates all the required steps.  

<video width="400" controlsList="nodownload" src="../video/SwitchNetworkTier.mp4" controls>  
</video>  
<br/>  
  
After restarting your virtual machine, please allow a few minutes for the new IP address to map to the hostname.  
  
