---
layout: GCNS
---
  
## Google Cloud Nightscout reinstall
[Google Cloud Nightscout](./GoogleCloud.md) >> How to reinstall     
<br/>  
  
#### **Non-desctructive reinstall**  
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
Deleting the virtual machine is almost never necessary and should only be a last resort.  
Once deleted, all files, including your MongoDB database, will be lost permanently with no way to recover them.  
  
If you are certain you need to delete it, follow the backup instructions above to secure all necessary files.  
  
Only proceed with [deleting the machine](./DeleteVM.md) if you have no other recovery options and have ensured you have backed up everything.  
  
