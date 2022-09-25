## Swap Size
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page) >> [Google Cloud](./GoogleCloud.md) >> [Swap Size](./SwapSize.md)  
  
If you installed Nightscout on Google Cloud early following our instructions, it's possibelthat your swap size is 500MB.  
Since then, we have increase the recommended size to 2GB.  We have also changed the recommended disk space from 10GB to 30GB.  
You cannot change the disk size from 120GB to 30GB easily.  The only way you can do that is by stopping your virtual machine, deleting it, and creating a new one.  If you do that, you will lose your database in Nightscout.  Please don't do that before backing up your database.  
But, you can easily increase the swap space.  This is how you can do that.  
    
Open a [terminal](./Terminal).  
In the terminal type free -h and enter.  
This will show you how muich swap space you have already.  If it is 2GB, there is no need to change anythng.  You can close the terminal.  
  
If the swap space is 500MB, let's increase it to 2GB.  After entering each command, wait for it to complete before proceeding to the next command.    
In the terminal, type sudo swapoff -a and enter.  
Type sudo fallocate -l 2G /var/SWAP and enter.  
Type sudo chmod 600 /var/SWAP and enter.  
Type sudo mkswap /var/SWAP and enter.  
Type swapon /var/SWAP and enter.  
Type free -h.  It should now show 2GB.  You can now exist the terminal.  
  
