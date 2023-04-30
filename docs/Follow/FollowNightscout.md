## Follow Nightscout
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Follow](../Follow_page.md) >> Nightscout  
  
You can easily set up xDrip to follow Nightscout.  
Under [hardware data source](../HardwareDataSource.md), select "Nightscout Follower".  
  
Then, under Settings &#8722;> Nightscout Follow URL, enter the following depending on what the Nightscout AUTH_DEFASULT_ROLES is set to.  
  
| AUTH_DEFAULT_ROLES | Nightscout Follow URL |  
| ------------------ | --------------------- |  
| readable | https://<span>hostname</span> |  
| denied | https://<span>API_SECRET@</span>hostname or https://<span>token@</span>hostname |  
  
