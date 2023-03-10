## Recover MongoDB
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >> Restore a system with full disk  
  
If you created your setup a long time ago (August 2022) with a 10GB disk and never updated to increase the disk space to 30GB, your system will sooner or later stop to work.  
The best solution is to update before you run into a problem.  
<br/>  
  
---  
  
#### **Increase disk size from 10GB to 30GB**  
Go to [Compute Engine](./ComputeEngine.md).  
Under the heading "Name", click on the instance name.  
![](./images/InstanceName.png)  
  
Click on "Edit" at the top to edit the instance.  
![](./images/EditInstance.png)  
  
If you cannot connect to the virtual machine using SSH any longer, this page explains how you can recover the MongoDB from it.  
This is a last resort.  If you can still connect to the machine, it is much easier to either create a [backup](./DatabaseBackup.md) or [copy](./NS_Transfer.md).  
  
  
