## Database Backup
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Backup  
  
You must have mongodb set up on the virtual machine for this to work.  That is the case if you have Nightscout running following [install](./NS_Install.md) instructions.  
  
Open a [terminal](./Terminal.md).  Copy and paste the following into the terminal and enter, where backupfile is the name you choose for the backup.  
  
<mark>mongodump --gzip --archive=backupfile</mark>  
  
A compressed archive file, with the name you have chosen, will be created in the current directory.  
  
You should transfer the backup off the virtual machine.  Otherwise, you will lose it if you delete the machine.  
To do that, click on download at the top on the terminal.  
  
![](./images/Download.png)  
Enter a full path to the backup file and click on "Download" to download it to the device you are accessing your virtual machine on now.  
  
If the terminal shows a [disconnect](./images/Disconnect.png) error, wait a minute and click on retry to reconnect.  Try again and this time, it will download.  
  
You should safekeep the backup.    
