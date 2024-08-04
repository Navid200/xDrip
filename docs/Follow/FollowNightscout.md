## Follow Nightscout
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Follow](../Follow_page.md) >> Nightscout  
  
You can easily set up xDrip to follow Nightscout by configuring the following settings:  
  
Select `Nightscout Follower` as the [Data Source](../HardwareDataSource.md).  
  
Go to `Settings` &#8722;> `Nightscout Follow URL` and enter the Nightscout Follow URL, based on the `AUTH_DEFAULT_ROLES` variable setting:  
  
| AUTH_DEFAULT_ROLES | Nightscout Follow URL |  
| ------------------ | --------------------- |  
| readable | https://<span>hostname</span> |  
| denied | https://<span>API_SECRET@</span>hostname <br/> &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp; or <br/> https://<span>Token@</span>hostname |  
  
* hostname: The address of the Nightscout site, excluding https:// at the beginning.  
* API_SECRET: The password used by the uploader to upload to Nightscout.  
* Token: A token that a Nightscout user can create to allow you to follow without disclosing their password.  
<br/>  
  
---  

#### **Configure Wake Delay**  
As Nightscout Follower, xDrip will attempt to read from Nightscout a maximum of two times. If it fails to get a new reading after two attempts, it will assume the master is disconnected and go back to sleep to conserve battery and data.  
However, if there is a delay in the master uploading data to Nightscout, that may be the reason for the failure.  
You can set a delay for xDrip to wait before reading from Nightscout to account for this potential upload delay. This can help ensure that xDrip reads the latest data from Nightscout rather than going to sleep with outdated information.  
![](./images/NS_Follow_Delay.png)  
  
![](./images/NS_Follow_Delay2.png)  
  
**Do you need this setting?**   
If you are satisfied with how the Nightscout follower currently works, no changes are necessary.    
  
**Benefits of adjusting delay**  
Adjusting the delay can reduce the time it takes for new readings from the person you are following to appear on your follower. If you see a red delay indicator on the Nightscout follow status page, adjusting the delay setting may help.  
![](./images/NS_Delay.png)  
  
**What if you don’t change anything?**  
If you don’t adjust the follower delay, your follower will still receive all readings, but there might be a delay of up to 5 minutes depending on the master’s upload time.  
  
**Best setting for delay**  
Most users can leave the setting at the default value of 0. If you notice a delay on the Nightscout follow status page, experiment with different delay values to find the shortest delay that eliminates the red delay indicator.  
  
