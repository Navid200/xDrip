## Nightscout Database Backup
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Backup  
  
You must have mongodb set up on the virtual machine for this to work.  That is the case if you have Nightscout running following [install](./NS_Install.md) instructions.  
  
Open a [terminal](./Terminal.md).  Select "Backup MongoDB" from the [menu](./Menu.md).  
A compressed archive file, with the name you have chosen, will be created in the current directory.  
  
You should transfer the backup off the virtual machine.  Otherwise, you will lose it if you delete the machine.  
To do that, exit the menu first.  Let's find out what directory you are in.  Type and enter pwd. You will see something like this:  
/home/jane  
By default, you would be in your home directory and if your Linux useID is jane, your home directory will be called jane and it will be in the home directory.  

Now, type and enter ls.  You should see the files in the jane directory including the backup file you just created.  Remember the filename.  
click on download at the top on the terminal.  
  
![](./images/Download.png)  
Enter a full path to the backup file.  For example, if the backup filename is backup_Nov042022, the full path will be /home/jane/backup_Nov042022.  
With the full path entered, click on "Download" to download the backup file to the device you are accessing your virtual machine on now.  
  
It is possible that the terminal will disconnect when you attempt a download and shows a [disconnect](./images/Disconnect.png) error.  Just wait a minute and click on retry to reconnect.  Try again and this time, it will download.  
  
You should safekeep the backup.    
