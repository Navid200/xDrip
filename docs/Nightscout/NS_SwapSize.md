## Swap Size
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page) >> [Google Cloud](./GoogleCloud.md) >> [Swap Size](./NS_SwapSize.md)  
  
If you installed Nightscout on Google Cloud early following our instructions, it's possible that your swap size is 500MB.  
Since then, we have increased the recommended size to 2GB.  We have also changed the recommended disk space from 10GB to 30GB.  
You cannot change the disk size from 10GB to 30GB easily.  The only way you can do that is by stopping your virtual machine, deleting it, and creating a new one.  If you do that, you will lose your database in Nightscout.  Please don't do that before backing up your database.  
But, you can easily increase the swap space.  This is how you can do that.  
    
Open a [terminal](./Terminal).  
In the terminal type free -h and enter.  
This will show you how much swap space you have already.  If it is 2GB, there is no need to change anything.  You can close the terminal.  
  
If the swap space is 500MB, let's increase it to 2GB.  Enter the following commands in sequence in the terminal.  After entering each command, wait for it to complete before proceeding to the next.  
  
sudo swapoff -a  
sudo fallocate -l 2G /var/SWAP  
sudo chmod 600 /var/SWAP  
sudo mkswap /var/SWAP  
sudo swapon /var/SWAP  
  
Type free -h and enter.  It should now show 2GB.  You can now exit the terminal.  
  
