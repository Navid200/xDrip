---
layout: GCNS
title: "Google Cloud Nightscout Backup Guide"
description: "How to back up your Nightscout MongoDB database and variables on Google Cloud: generate a compressed archive via the terminal, download it safely, and avoid data loss if the VM is deleted."
---

# Google Cloud Nightscout Backup
[Google Cloud Nightscout](./GoogleCloud.md) >> Backup  
  
You must have Nightscout [installed and operational](./GoogleCloud.md) for this to work.  
A video tutorial is included to complement the instructions.  
  
**Backup**  
Open a [terminal](./Terminal.md), and select "Backup MongoDB and variables" from the **Data** submenu.  
A compressed archive file will be created in the current directory, containing the MongoDB database and variables.  
  
**Transfer the backup**  
To prevent loss if the virtual machine is deleted, transfer the backup off the machine.  
After the backup is completed, the full path to the backup file will be displayed on the screen—copy it.  
  
Click **“Download File”** at the top of the terminal.    
![Download](./images/Download.png)  
  
Enter the full path to the backup file, then click **“Download”** to start the transfer to your local device.  
  
If the terminal disconnects during the download and shows a [disconnect error](./images/Disconnect.png), wait a moment and click **“Retry”** to reconnect. Then, try downloading again.  
  
Ensure you store the backup file in a safe location.  
  
<video width="400" controlsList="nodownload" src="./video/Backup.mp4" controls>  
</video>  
  
<br/>  
  
---  
  
[Restore](./DatabaseRestore.md)  
  
