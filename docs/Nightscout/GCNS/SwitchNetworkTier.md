---
title: "Switch Network Tier in Google Cloud Nightscout"
description: "Learn how to change your Google Cloud Nightscout instance's Network Service Tier from Premium to Standard to qualify for free outbound traffic. Includes step-by-step instructions and video guide."
layout: GCNS
---

# Network Service Tier    
[Google Cloud Nightscout](../GoogleCloud.md) >> Network Service Tier  
<br/>  
  
By default, the Network Service Tier is set to Premium, which can lead to higher costs for outgoing traffic. Switching to the Standard tier allows up to 200 GB of outbound traffic per month at no charge.  
Our installation guide now includes instructions for changing the service tier from Premium to Standard.  
<br/>  
  
---  
  
**To check** which service tier your virtual machine is using, refer to the video below:  
  
<video width="400" controlsList="nodownload" src="./video/IdentifyNetworkTier.mp4" controls>  
</video>  
<br/>  
  
---  
  
**To change** the network service tier of an active machine, you must stop it first.  
The following video walks you through the entire process:  

<video width="400" controlsList="nodownload" src="../video/SwitchNetworkTier.mp4" controls>  
</video>  
<br/>  
  
Once your virtual machine has restarted, please allow a few minutes for the new IP address to update and map to the hostname.  
  
