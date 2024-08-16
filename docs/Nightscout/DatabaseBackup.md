---
layout: GCNS
---

## Google Cloud Nightscout Backup
[Google Cloud Nightscout](./GoogleCloud.md) >> Backup  
  
You must have Nightscout [installed and operational](./GoogleCloud.md) for this to work.  
A video clip is included to assist with the instructions.  
  
**Backup**  
Open a [terminal](./Terminal.md), and select "Backup MongoDB and variables" from the Data submenu.  
A compressed archive file with the name you choose will be created in the current directory, containing the MongoDB database and variables.  
  
**Transfer the backup**  
To prevent loss if the virtual machine is deleted, transfer the backup off the machine.  
  
Exit the menu to return to the command line.  
  
Type `pwd` and press Enter to see the path, which will look like /home/jane. By default, you will be in your home directory. If your Linux user ID is jane, your home directory will be named jane and located under /home.  
  
Type `ls` and press Enter to list the files in your directory, including the backup file you just created. Note the filename.  
  
Click “Download” at the top of the terminal.    
![](./images/Download.png)  
Enter the full path to the backup file. For example, if the backup filename is backup_Nov042022, the full path would be /home/jane/backup_Nov042022.  
  
Click “Download” to start downloading the backup file to the device you’re using to access the virtual machine.  
  
If the terminal disconnects during the download and shows a [disconnect error](./images/Disconnect.png), wait a moment and click “Retry” to reconnect. Try downloading again, and it should proceed.  
  
Ensure you store the backup file in a safe location.  

<video width="400" controlsList="nodownload" src="./video/Backup.mp4" controls>  
</video>  
  
<br/>  
  
---  
  
[Restore](./DatabaseRestore.md)  
  
