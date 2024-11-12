## Nightscout Uploader Settings
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Nightscout](Nightscout_page.md) >> Nightscout master
  
To enable the Nightscout uploader (master), follow these steps:  
  
1- Go to `Settings` &#8722;> `Cloud Upload` &#8722;> `Nightscout Sync (REST-API)`.  
  
![](./images/NightscoutPage.png)  

2- At the top of the page, enable the function.  
  
3- Tap on `Base URL` and enter the format:  
https<nolink>://password@hostname/api/v1/  

- "password" is the "API_SECRET" you can find in your Nightscout variables.  
- "hostname" is the URL you use to access Nightscout in a browser (excluding https<nolink>://).  
<br/>  

Example:  
If your hostname and password are as follows:  
- Hostname: `jane.us.to`  
- Password: `3MyPassword4NS`  

Then, the correct URL will be:  
https<nolink>://3MyPassword4NS<nolink>@jane.us.to/api/v1/  
<br/>  
  
---  

#### **[Nightscout](./Nightscout_page.md)**  
<br/>  
  
---  
  
#### **Disable a follower**  
To disable a follower, you can remove the token associated with that follower.  
- If multiple followers use the same token, you’ll need to change it and inform those you want to continue following you.
- If you’ve shared your API_SECRET with the follower, you’ll need to update it.  
<br/>  

---  
  
#### **Upload to multiple Nightscout sites**  
To upload to multiple Nightscout sites, enter the URLs in the same field, separated by spaces.  
  
