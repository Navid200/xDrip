---
layout: GCNS
---
  
## Google Cloud Nightscout reinstall
[Google Cloud Nightscout](./GoogleCloud.md) >> How to reinstall   
  
To reinstall, you need to run bootstrap followed by phase 1 (installation).  Both are explained [here](./NS_Install.md).  
After that, [Restart](./Restart.md) the server.  
  
<br/>  
  
---  
  
#### **Backup**  
Follow the instructions [here](./DatabaseBackup.md) to backup the database and download it from the machine to your computer.  
If you have any other files on the virtual machine that you like to keep, download those as well.  
<br/>  
  
---  

#### **Restore**
Follow [these](./DatabaseRestore.md) instructions to restore the MongoDB and your variables.  
<br/>  
  
---  
  
#### **Deleting the machine**  
Deleting the virtual machine is hardly ever necessary.  You shouldn't do it.  It is only a last resort.  

If you delete the virtual machine, all the files on it will be deleted and you will have no way of recovering them.  This includes your MongoDB database.  
Therefore, if you are certain you need to delete it, follow the backup instructions above to backup everything including your database.  
  
If you are certain you have backed up everything and have all the files you need, only then, should you [delete the machine](./DeleteVM.md) only if there is no way to restore it.  

Now, you can follow the [guide](./GoogleCloud.md) to recreate a new machine.  Then, you can restore your backup on the new machine.  
<br/>  
  
