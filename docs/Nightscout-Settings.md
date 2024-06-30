## Nightscout Uploader Settings
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Nightscout](Nightscout_page.md) >> Nightscout master
  
To enable Nightscout uploader (master), go to the following page.  
`Settings` &#8722;> `Cloud Upload` &#8722;> `Nightscout Sync (REST-API)`  
  
![](./images/NightscoutPage.png)  

At the top of the page, enable the function.  
Tap on `Base URL`.  Enter the following:  
https<nolink>://password@hostname/api/v1/  

"password" is the "API_SECRET" you can find in your Nightscout variables.  
"hostname" is the URL you use to access Nightscout in a browser excluding https<nolink>://  
<br/>  
<br/>  
  
---  

#### **[Nightscout](./Nightscout_page.md)**  
<br/>  
  
---  
  
#### **Disable a follower**  
To disable a follower from following you, you will need to remove the token corresponding to the follower.  
If the follower uses the same token used by other followers, you will have to change the token and inform the followers that you want to continue to follow you.  
If you have given your API_SECRET to the follower, you will need to change your API_SECRET.  
<br/>  

---  
  
#### **Upload to multiple Nightscout sites**  
Enter the URLs in the same field, separated by space.  
  
