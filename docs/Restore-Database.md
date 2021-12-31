To restore a database, you need to place it in the xdrip folder on the phone internal storage.  If the folder does not exist, you can create one.  
The database filename is composed of the word export followed by year, month, day, hours, minutes, seconds as shown on the following line.  
exportYYYYMMDD-HHMMSS.zip

Be very careful. xDrip cannot _import_ a database.  It has a restore database function.  What it does is to replace the existing database with the one being restored.  
Effectively, xDrip deletes the entire existing database and then replaces it with the new database. 

To import the database, tap on Import / Export features -> Restore database.  
Read the warning.  If it does not make you change your mind about restoring a database, tap on OK.  
Tap on the file name to restore.  

If your phone has Android version 11 or 12, you may experience an [issue](./Database-restore-failure).  
