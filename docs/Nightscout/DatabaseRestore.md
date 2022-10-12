## Database Restore
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Restore  
  
You must have mongodb set up on the virtual machine for this to work.  That is the case if you have Nightscout running following [install](./GoogleCloud.md) instructions.  
  
Open a [terminal](./Terminal.md).  
Click on upload at the top on the terminal.  
  
![](./images/Upload.png)  
Select the baclup file you created following [these](./DatabaseBackup.md) instructions, and click on "Upload".  
The file will be uploaded into the virtual machine in the working directory.  

Copy and paste the following into the terminal and enter, where backupfile is the name of the backup file you want to restore.  
  
<mark>mongorestore --archive=backupfile</mark>  
  
An archive file, with the name you have chosen, will be created in the current directory.  
  
You should transfer the backup off the virtual machine.  Otherwise, you will lose it if you delete the machine.  
To do that, click on download at the top on the terminal.  
  
![](./images/Download.png)  
Enter a full path to the backup file and click on "Download" to download it to the device you are accessing your virtual machine on now.  
  
If the terminal shows a [disconnect](./images/Disconnect.png) error, wait a minute and click on retry to reconnect.  Try again and this time, it will download.  
  
You should safekeep the backup.    
