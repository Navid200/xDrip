## How to reinstall
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> How to reinstall  
  
If you have made a mistake when you created the virtual machine and need to recreate it, you should backup first to avoid losing everything when you delete the machine.  
  
Follow the instructions [here](./DatabaseBackup.md) to backup the database and download it from the machine to your computer.  
Download the following file as well: /etc/nsconfig

If you have any other files on the virtual machine that you like to keep, download those as well.  
Now, delete the machine.  
Carefully follow [this guide](./GoogleCloud.md) to create the machine and install Nightscout.  
  
Upload the nsconfig file.  Restore nsconfig by entering the following in the terminal.  
<mark>sudo mv -f onfig
