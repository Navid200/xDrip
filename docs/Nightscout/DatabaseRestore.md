## Database Restore
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Restore  
  
You must have mongodb set up on the virtual machine for this to work.  That is the case if you have Nightscout running following [install](./GoogleCloud.md) instructions.  
  
Open a [terminal](./Terminal.md).  
Click on upload at the top on the terminal.  
  
![](./images/Upload.png)  
Click on "Choose Files".  Select the baclup file you created following [these](./DatabaseBackup.md) instructions, and click on "Upload".  
The file will be uploaded into the virtual machine in the working directory.  

Copy and paste the following into the terminal and enter, where backupfile is the name of the backup file you want to restore.  
  
<mark>mongorestore --gzip --archive=backupfile</mark>  
  
The backup content will be added to the database.     
  
  
