## Database Corruption  

Users have reported xDrip database corruption.  
[Open issue](https://github.com/NightscoutFoundation/xDrip/issues/1280)  

We don't know what causes the corruption, and don't have a fix yet.  
<br/>  

---  

#### **What is the outcome if my database is corrupted?**  
If this happens, you will lose your database losing all your old readings.  
Your sensor may stop working until you attend to it.  
There will be no way to recover the old readings.  
<br/>  
  
---  
  
#### **What should I do?**  
1- Enable "Missed reading alert", which by default is disabled.  Then, you will have an alert if readings stop.  
  
2- Set up xDrip to automatically upload to the cloud.  You can use Nightsocut or Tidepool for that.  
Then, your data will be safe regardless of what happens to your xDrip database.  
Considering you could lose your phone, this would be strongly recommended even if there was no possibility of database corruption.  
  
3- We have a daily automatic backup into Google Drive option.  [Google Drive Backup](https://navid200.github.io/xDrip/docs/GoogleDriveBackup.html)  
You can enable this so that a backup of your database and all settings will automatically be made on a daily basis.  
  
4- We have a daily automatic database backup onto the phone local storage option.  [Daily legacy database backup](https://navid200.github.io/xDrip/docs/Backup/SaveDaily.html)  
You can enable this so that a database backup is made on a daily basis and saved on your phone.  
This however can eventually fill up the disk on your phone.  Then, you can manually delete the old saved files to free up space.  
  
