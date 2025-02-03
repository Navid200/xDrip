---
layout: GCNS
---

## Google Cloud Firewall  
[Google Cloud Nightscout](../GoogleCloud.md) >> Firewall  
<br/>  
  
When you create a virtual machine, it has a firewall already.  To see it, go to your Google Cloud [Dashboard](../Dashboard.md).  
From the menu, select `VPC Network` > `Firewall`.  
![](./Firewall/images/AccessFirewall.png)  
  
If you have followed our instructions to create a virtual machine, you will see the following.  
![](./Firewall/images/FirewallRules.png).  
<br/>  
  
---  
  
#### **Firewall rules**  

If you have not created a virtual machine, you will only see the 4 rules at the bottom.  
The two rules at the top are related to the http and https options you are supposed to enable during the virtual machine setup following our instructions.  
<br/>  
  
---  
  
#### **Firewall policies**  
If you want to use geo-location, you will need to create a firewall policy.  But, geo-location is not free.  

Delete the http and https rules.  Now, you will not be able to access your site.  Test and verify.  
  
Then, create a firewall policy.  
![](./Firewall/images/CreateFirewallPolicy.png)  
<br/>  
<br/>  

Give it a name.  Continue.  
![](./Firewall/images/CreateANetworkFirewallPolicy.png)  
<br/>  

Continue.  
![](./Firewall/images/ConfigurePolicy2.png)  
<br/>  
  
Associate it and save.  
![](./Firewall/images/FirewallPolicyAssociation.png)  
<br/>  
  
Now, create a new rule inside the firewall policy you created.  
In the geo-location section, choose your country making sure it is an "allow" rule.  Geo-location is not free.  
![](./Firewall/images/AllowFirewallRuleDetails.png)  
<br/>  
  
![](./Firewall/images/FirewallPolicyRules.png)  
  
  
