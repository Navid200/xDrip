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

If you delay the Nightscout follower this way such that when it attempts to read from Nightscout, the master has already uploaded, your follower will succeed to read instead of going to sleep and getting the same reading with a few minutes of delay.  

**Do I need this?**   
If you are happy with how Nightscout follower works for you, no, you can keep everything as is.  

**What is the benefit?**  
It can decrease the delay from the time the person you are following has a new reading until you can see it on your follower.  
If you look at the Nightscout follow status page and see a delay in red as shown below, it means that you may benefit from this.  
![](./images/NS_Delay.png)  

**What if I don't change anything?**  
If your master experiences a delay and you don't adjust the follower delay, your follower will still receive all readings.  It will just receiver every reading with some delay.  The delay could be a few up to 5 minutes.  

**What is the best setting?**  
The majority can leave this at the default 0 setting.  
However, if you see a delay on the Nightscout follow status page, try different values.  Choose the shortest delay that removes the red delay note from the status page.  
  
