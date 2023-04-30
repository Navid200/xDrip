## Follow Nightscout
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Follow](../Follow_page.md) >> Nightscout  
  
You can easily set up xDrip to follow Nightscout.  
Under [hardware data source](../HardwareDataSource.md), select "Nightscout Follower".  
  
Then, under Settings &#8722;> Nightscout Follow URL, enter the following depending on what the Nightscout AUTH_DEFAULT_ROLES variable is set to.  
  
| AUTH_DEFAULT_ROLES | Nightscout Follow URL |  
| ------------------ | --------------------- |  
| readable | https://<span>hostname</span> |  
| denied | https://<span>API_SECRET@</span>hostname or https://<span>Token@</span>hostname |  
  
The hostname is the address of the Nightscout site excluding https:// at the beginning.  
API_SECRET is the password the uploader uses to upload to Nightscout.  
Token is what Nightscout user can create to allow you to follow without having to disclose their password.  
