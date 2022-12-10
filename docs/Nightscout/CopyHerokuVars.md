## Copy Heroku Variables
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Copy Heroku Variables  
  
It's always best to adjust variables/settings for each device/setup individually.  However, there are times you may have too many variables and you prefer to copy everything first.  
    
We have a utilty that copies all your Heroku variables into a file.  You can then view and edit it first and then, add it to your variables, which are in /etc/nsconfig.  
However, you can use this utility only if you have not enabled multi-factor autherntication on Heroku.  
You will need your Heroku user ID (email) and password.  To run the utility, enter the following into the [terminal](./Terminal.md).  
  
```  
/xDrip/scripts/GetHerokuVars.sh
```  
<br/>  
  
Alternatively, you can log into Heroku and access your variables as explained [here](./HerokuVars.md).  
  
