---
layout: GCNS
---

# Google Cloud cost  
[Google Cloud Nightscout](./GoogleCloud.md) >> Unexpected costs    
  
#### **Why have I been charged?**  
Nightscout on Google Cloud is free with a few exceptions. A credit card is required to set up billing.  
This page explains the details and highlights incorrect settings that could lead to expenses.  
To determine the exact reason for any charges, create a [cost table](./CostTable.md).  
<br/>  
  
---  
  
#### **Do you have more than one virtual machine?**  
To stay within the free tier, you may only run **one** virtual machine per Google account. If you need more than one Nightscout instance, you will need a separate Google account for each, as explained [here](./MultipleAccounts.md).  
<br/>  
  
---  
  
#### **Free tier Constraints**
When creating a virtual machine, follow the installation guide to ensure compliance with free tier requirements.  
On the status page, violations of these requirements will be highlighted in red above the horizontal line.  
  
You can have a free virtual machine only in specified regions, but this doesn’t restrict the location of your followers.  
A free virtual machine is available only in specific regions, but this does not restrict your location or the location of your followers.  
<br/>    
  
---  
  
#### **Are you using Premium Network Service Tier?**  
Using the Premium Network Tier may result in charges.  You can learn how to check and, if necessary, switch the tier [here](./GCNS/SwitchNetworkTier.md).  
<br/>  
  
---  
  
#### **[Storage PD Snapshot](./SnapshotCost.md)**
<br/>  

---  

#### **[Storage PD Capacity](./StoragePDCap.md)**
<br/>  
  
---  

#### **[Multiple Nightscout accounts](./MultipleAccounts.md)**
<br/>  
  
---  
  
#### **Free Trial**
When you first sign up for Google Cloud, you can enable the free trial program, which gives you $300 in credits for a 3-month period. During this time, any costs will be covered by this credit. You can monitor your spending through the billing dashboard.  
If your costs are higher than expected, stopping the virtual machine before the trial ends can help avoid actual charges.  
