## Copy Heroku Variables
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Copy Heroku Variables  
  
It's always best to adjust variables/settings for each device/setup individually.  However, there are times you may have too many variables and you prefer to copy everything first.  
This may result in some variables, in the new setup, that may need to be changed or deleted.  
  
You have two options. 
  
1- We have a utilty that copies all your Heroku variables into a file.  You can then view and edit it first and then, add it to your variables, which are in /etc/nsconfig.  
However, you can use this utility only if you have not enabled multi-factor autherntication on Heroku.  
You will need your Heroku user ID (email) and password.  To run the utility, enter the following into the terminal.  
  
<mark style="background-color: #eFdFef">/xDrip/scripts/GetHerokuVars.sh</mark>  
<br/>  
  
2- Log into Heroku.  
Go to the dashboard.  Click on your app that contains your Nightscout.  Click on "Settings".  Click on "Reveal Config Vars".  
You can now see all your variables.  
Open a [terminal](./Terminal.md).  Use the menu to edit variables.  
Copy the variables you need from Heroku to your Nightscout on Google Cloud.  
After you are done, you will need to restart the server for the changes to the variables to take effect.  
 
