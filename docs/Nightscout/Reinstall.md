---
title: "Google Cloud Nightscout Reinstall"
description: "Learn how to safely reinstall your Nightscout instance on Google Cloud as a final troubleshooting step—when all else fails. Includes documentation, help, and best practices for avoiding data loss."
layout: GCNS
---
  
# Google Cloud Nightscout Reinstall
[Google Cloud Nightscout](./GoogleCloud.md) >> How to reinstall     
<br/>  
  
#### **Non-Destructive Reinstall**  
You can reinstall from scratch over an existing installation without affecting your database.  
To do this, run bootstrap followed by phase 1 (installation)—both are explained [here](./NS_Install.md).  
Afterward, [restart](./Restart.md) the server. You can then proceed with phase 2, as explained in the same guide.  
<br/>  
<br/>  
  
---  
  
#### **Backup**  
If you delete your virtual machine, you will lose everything on it, including your Nightscout database.  
To prevent data loss, follow the [backup instructions](./DatabaseBackup.md) to back up your database and download it to your computer.  
If you have other important files on the virtual machine, be sure to download those as well.   
<br/>  
  
---  
  
#### **Snapshot**  
You can also create a [snapshot](./Snapshots.md), which saves a compressed version of the disk. This allows you to restore the entire system when creating a new virtual machine.  
Note: Snapshots are not free and may cost around $0.25 per month while stored.  
<br/>  
  
---  
  
#### **Deleting the Virtual Machine**  
Deleting the virtual machine is rarely necessary and should only be a last resort.  
Once deleted, all files, including your MongoDB database, will be lost permanently with no way to recover them.  
  
If you are certain you need to delete it, follow the backup instructions above to secure all necessary files.  
  
Only proceed with [deleting the machine](./DeleteVM.md) if you have no other recovery options and have ensured you have backed up everything.  
<br/>  

---  
  
#### **Reinstall**  
Follow the [installation instructions](./GoogleCloud.md) to set up a new instance.  
Once completed, you can [restore your backup](./DatabaseRestore.md).  
