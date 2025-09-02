---
title: "Google Cloud Virtual Machine for Nightscout"
description: "Step-by-step instructions to create a free-tier Google Cloud virtual machine optimized for hosting Nightscout safely and efficiently."
layout: GCNS
---
  
# Google Cloud Virtual Machine  
[Google Cloud Nightscout](./GoogleCloud.md) >> Virtual Machine  
   
In this guide, we will create a free virtual machine in our [Google project](./NS_GCProject.md).  Each Google account is eligible for one free virtual machine.  
For additional assistance, a video clip on this page may be helpful. This process takes around 5 minutes.  
<br/>  
  
---
  
### ⚠️ WARNING!  
This virtual machine is strictly for hosting Nightscout. Do not use it for work, banking, trading, shopping, or development.   
<br/>  
  
---  
  
Go to "Compute Engine"  &#8594; "VM Instances".  
  
Select "Create Instance".  
![Create Instance](./images/CreateInstance.png)  
  
Select all the items mentioned below.  Leave everything else as is.  
<br/>  
  
---
  
#### **Machine configuration**  
Select a region that qualifies for free tier.  Those are ![Oregon](./images/Oregon.png), ![Oregon](./images/Iowa.png) or ![S_Carolina](./images/S_Carolina.png).  
  
Set machine type to "e2-micro".  
![Machine Type](./GCNS/images/MachineType.png)  
<br/>  
  
----  
  
#### **OS and storage**  
Under "OS and storage", click on Change and modify as shown below.  
![Disk2](./images/Disk2.png)  
  
Click on "Select".  
<br/>  
  
---  
  
#### **Data protection**  
Under Data protection, choose "No backups".  
<br/>  
  
---  
  
#### **Networking**  
Under "Firewall", enable both http and https.  
![Firewall2](./images/Firewall2.png)  
  
Under "Network interfaces", click on "default".  In the fields that appear, under "Network Service Tier", select "Standard".  
Click on "Done".  
  
<br/>  
  
---  
  
#### **Required Configuration Checklist**  
Before clicking on create,  ensure the following settings are correct:  
  
| Parameters | Requirements | Why |  
| ---------- | ------------ | ---- |  
| Machine Region    | Oregon, Iowa or South Carolina | Free Tier |  
| Machine Type | e2-micro | Free Tier |  
| Disk type    | persistent disk | Free Tier |  
| Disk size   |  Less than or equal to 30GB | Free Tier |  
| Operating system | Ubuntu | Compatibility |  
| Version | 24.04 LTS Minimal x86 /64, amd64 noble ... | Nightscout compatibility |  
| Data protection | No backups | Free |  
| Firewall | Allow HTTP traffic | Nightscout access |  
| Firewall | Allow HTTPS traffic | Nightscout access |  
| Network Service Tier | Standard | Free |  
  
<br/>  
  
---  

Click on "Create" to create the virtual machine.  Google will now initialize your virtual machine.  Wait for it to come up.  It could take a minute.  When the external IP column appears, you should be good to go.  Your virtual machine is now ready! 🎉  
<br/>  
  
<video width="400" controlsList="nodownload" src="./video/VM.mp4" controls>  
</video>  
  
<br/>  
<br/>  
  
---  
    
The estimate shown at the top right on the same page will not be 0.  As long as you satisfy the conditions listed in the above table, it's OK.  You can ignore that non-0 estimate.  
You can also use the [calculator](https://cloud.google.com/products/calculator) to confirm the price.  
![Estimate](./images/Estimate.png)  
<br/>  
<br/>  
  
---  
  
### ❌ Handling Virtual Machine Creation Errors  
If you see a red exclamation mark after creating your virtual machine and hovering over it shows an error message stating that the instance is unavailable, follow these steps:  

1- Dismiss the request by clicking the trash can icon on the right.  
2- Reattempt creation in a different region.  
3- Unfortunately, you will need to reconfigure all settings from scratch for the new request.  
![VM_Unavailable](./images/VM_Unavailable.png)   
<br/>  
  
---  
  
  
