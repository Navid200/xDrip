## Google Cloud snapshots
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Google Cloud](./GoogleCloud) >> Snapshots  
  
You can create a snapshot of the virtual machine disk.  Then, if you experiement and run into trouble, you can always restore the snapshot to reurn the disk to the way it was ehen you created the snapshot.
Of course this means any data added to the database since the snapshot was created will be lost also.  So, you should use this utility taking into account that consequence.  
<br/>  
  
---  
**Create**  
Let's go through the steps.  Let's say we have run bootstrap, the first step in installation.  And let's create a snapshot.   
  
When on the Compuete Engine page, click on Snapshots in the left pane.  
![](./images/Snapshots.png)  
  
Click on create snapshot at the top.  
![](./images/Snapshots2.png)  
  
Select the disk in your virtual machine from the pull-down menu under "Source disk".  
![](./images/SourceDisk.png)  
  
If you have just completed an install phase, you may see nothng under the pull-down menu.  Just return to the dahsboard and go through the menu items to get back to the snapshot creation page.  
  
Give a name (all lower case) to the snapshot so that you can easily identify it later.  
![](./images/SnapshotTitle.png)  
  
Make sure to select Regional under Location.  By default, Multi-regional is selected.  Otherwise, there may be a cost.  
![](./images/SnapshotRegion.png)  
  
Click on "Create" at the bottom of the page.  
Depending on how much data you have on the disk, this could take a different amount of time to complete.  For a fresh install with no database imported yet, it take about a minute.  
  
Pay attention to the total size of your snapshots.  If you go over a limit (5GB), it will not be free.  
![](./images/Snapshots3.png)  
  
The following screenshot shows the free tier limits.  
![](./images/FreeTierSnapshot.png)  
<br/>  
  
---  
  
**Restore**
Let's now go through the steps of restoring a snapshot.  
  
**WARNING**
You can have only one machine in order to satisfy the free tier limits.  Therefore, in order to restore a snapshot, you will need to first delete your existing machine.  Doing that (deleting your machine) will delete your Nightscout database.  
Please don't proceed if you are not sure what you are doing.  
This may be a useful tool to have while yo uare experimenting with your choices and have not copied your database yet.  
  
  
