---
title: "xDrip Database Corruption Troubleshooting"
description: "Learn how to prevent and recover from database corruption in xDrip, including enabling missed reading alerts, setting up cloud backups, and configuring daily local backups."
---

# Database Corruption  

Users have reported xDrip database corruption.  
[Open issue](https://github.com/NightscoutFoundation/xDrip/issues/1280)  

The exact cause of this issue is unknown.  
<br/>  

---  

#### **What happens if my database is corrupted?**  
If this occurs, you will lose all your old readings.  
Your sensor may stop working until you address the issue.  
There will be no way to recover the lost readings.  
<br/>  
  
---  
  
#### **What should I do?**  
1- Enable "Missed reading alert" – This is disabled by default.  Enabling it ensures you receive an alert if readings stop.  
  
2- Set up automatic cloud uploads – You can use Nightscout or Tidepool for this.  
This ensures your data is safe even if the xDrip database is corrupted.  
Given the risk of losing your phone, this is strongly recommended regardless of database issues.  
  
3- Enable daily automatic Google Drive backups – [Google Drive Backup](https://navid200.github.io/xDrip/docs/GoogleDriveBackup.html)  
This option creates daily backups of your database and all settings to Google Drive automatically.  
  
4- Enable daily automatic local backups on the phone – [Daily legacy database backup](https://navid200.github.io/xDrip/docs/Backup/SaveDaily.html)  
This option creates daily backups of your database on your phone.  
Be aware that over time, these backups can fill up your phone’s storage.  You can manually delete older files to free up space.  
  
