## Transfer to a new phone  
[xDrip](../README.md) >> [Download & Installation](./Installation_page) >> [Transfer to a new phone](./New-Phone)  
  
If you like to have your previous readings on your new phone, this is about how you should do it.  
  
Be very careful. xDrip cannot _import_ a database.  It has a [restore](./Restore-Database.md) database function.  What it does is to replace the existing database with the one being restored.  
Effectively, xDrip deletes the entire existing database and then replaces it with the new database.  

The best course of action is to restore the database into the new phone before setting it up as a collector.  Otherwise, if you start collection on the new phone, restoring a database after will overwrite every collection.  The following list shows the recommended order.  
  
1. [Export](./Backup-Database.md) the database from the old phone.  
2. Install xDrip on the new phone.  
3. Transfer the exported database to the new phone.  You can email it to yourself, or use a USB cable and a computer.   
4. Create an xdrip folder on the new phone, if it doesn't exist, and place the database file in it.    
5. [Restore](./Restore-Database.md) the database only if you have not already started collection, or if you are willing to lose the new readings.  
This will restore the readings history and your alerts.  
6. Set up the new phone as your new collector.  You should disable collection on the old phone first.  
7. If you want to bring in more settings from the old phone, you need to load the settings file from the old phone as well.  

This is not meant to convince you to use your phone as a repository of all your previous readings.  A better repository for your readings is [Nightscout](./Nightscout.md).  Because it is independent of your phone, which you may replace or lose.  You can use Nightscout to access previous readings and perform statistical analyses and use them for creating reports.  
