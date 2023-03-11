## Google Cloud 30GB Disk
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >> Restore a system with full disk  
  
If you created your setup a long time ago (August 2022) with a 10GB disk and never updated to increase the disk space to 30GB, your system will sooner or later stop to work.  
The best solution is to update before you run into a problem.  
<br/>  
  
---  
  
#### **Increase disk size from 10GB to 30GB**  
Go to [Compute Engine](./ComputeEngine.md).  
Under the heading "Name", click on the instance name.  
![](./images/InstanceName.png)  
  
Scroll down to "Storage".  After confirming that the disk apce is 10GB, click on the disk name.  
![](./images/EditInstanceDisk.png)  
  
Now, you need to expand your browser screen horizontally until you can see "Edit" at the top.  Click on "Edit".  
![](./images/EditDisk.png)  
  
Edit disk size.  
![](./images/Disk10G.png)  
  
Change it to 30GB and save.  
![](./images/UpdateDiskSize.png)  
<br/>  
  
---  
  
#### **Restart the server**
You now need to [restart the server](./Restart.md) so that the filesystem uses the added disk space.  
<br/>  
  
---  
  
#### **Full disk**
If you cannot connect to the virtual machine using SSH because the disk is full, even after increasing the disk size to 30GB, you may still not be able to connect.  
This is because Ubuntu needs to adjust the filesystem to use the added disk space.  But, if Ubuntu cannot start, it will not be able to do that.  
  
We are going to create another (recovery) instance to use temporarily to fix this problem.  
There may be a cost involved.  I will use what's needed to minimize cost.  Hopefully, it will be free.  
  
- 1- Create a new instance.  
Follow the same [guidelines](./VirtualMachine.md) for creating the main machine except there is no need to change the disk space to 30GB.  You can leave it at 10GB for the recovery machine.  

- 2- Use SSH to connect to the new machine.  This is very important.  Please don't skip this step.  
 
- 3- Stop the Nightscout instance using the 3-dot menu on the right side of the instance line on the Compute Engine page.  This may take up to 3 minutes to complete.  
 
- 4- Click on the stopped Nighsctout instance name.  
![](./images/StoppedInstanceSelect.png)  

- 5- Click on Edit at the top.  
![](./images/EditInstance.png)  

- 6- Scroll down to "Storage".  Click on "Detach boot disk".  Click on "Save".  
![](./images/DetachBootDisk.png)  

- 7- Go back to VM instances.  
![](./images/VM_Instances.png)  

- 8- Click on the recovery instance name.  
![](./images/EditRecoveryInstance.png)  

- 9- Click on Edit at the top.  Scroll down to Storage.  Scroll further down to "Additional disks".  Click on "Attach existing disk".  If you see a note that there is no disk to attach, it means you did not detach the boot disk of your Nightscout instance.  Go back and do that.  
![](./images/AttachExistingDisk.png)  

- 10- From the pull-down menu, select the disk of your Nightscout instance.  Click on save.  That will save the disk setting of the instance.  But, you are still on the instance edit page.  Click on save again to finalize editing the instance.  
![](./images/SelectExistingDisk.png)  
Now, you have a new instance with two disks attached to it.  One is its boot disk.  The other is the problem disk that we are going to fix.  

- 11- Go back to the VM instances page again.  Click on SSH on the temporary (recovery) instance line.  Wait for a terminal to pop up and the prompt to appear.  Type and enter the following in the terminal.  
```  
sudo lsblk  
```  
![](./images/lsblk1.png)  
sda is the boot disk of the temporary (recovery) instance.  sdb is the trouble disk.  
Even though sdb shows 30GB, sdb1, which is the partition containing Ububtu and MongoDB still shows 9.9GB.  This is why we ccouldn't log into the Nightscout instance.  
  
  
