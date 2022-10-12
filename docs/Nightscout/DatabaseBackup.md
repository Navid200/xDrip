## Database Backup
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Backup  
  
You must have mongodb set up on the virtual machine for this to work.  That is the case if you have Nightscout running following [install](./GoogleCloud.md) instructions.  
  
Open a [terminal](./Terminal.md).  Copy and paste the following into the terminal and enter, where backupfile is the name you choose for the backup.  
  
<mark>mongodump --archive=backupfile</mark>  
  
  
