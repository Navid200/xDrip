## How to reinstall
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> How to reinstall   
  
If you have made a mistake when you created the virtual machine and need to recreate it, you should backup first to avoid losing everything when you delete the machine.  
You don't have to do that if you have just created the machine.  But, if you have used the machine for a while, or, if you have made progress in installation, you may have files on the machine that you may want to backup.  
If you delete the virtual machine, all the files on it will be deleted and you will have no way of recovering them.  This includes your MongoDB database.  
  
Follow the instructions [here](./DatabaseBackup.md) to backup the database and download it from the machine to your computer.  
Download the following file as well: /etc/nsconfig

If you have any other files on the virtual machine that you like to keep, download those as well.  
If you are certain you have all the files you need, only then, should you delete the machine.  
  
Carefully follow [this guide](./GoogleCloud.md) to create a new machine, and install Nightscout.  Set up Nightscout and xDrip to upload.  

Follow [these](./DatabaseRestore.md) instructions to restore the MongoDB backup.    
Upload the nsconfig file the same way.  Restore nsconfig by entering the following in the terminal.  
<mark style="background-color: #eFdFef">sudo mv -f nsconfig /etc/nsconfig</mark>  
  
[Restart](./Restart.md) the server.  
After restart, your Nightscout must be running with your data in it.  
  
What will be missing is anything that xDrip collected while you were working on the reinstall.  To upload those as well, use Back-fill data from xDrip Settings &#8722;> Cloud Upload &#8722;> Nightscout Sync (REST-API) &#8722;> Extras Options.  
