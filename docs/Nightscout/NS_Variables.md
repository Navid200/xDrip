## Nightscout variables  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> [Variables](./NS_Variables)  
  
If you need to edit your Nightscout variables, Go to the Google Cloud dashboard.  
Go to compute engine.  
Click on "SSH" on the line corresponding to your Nightscout engine.  
That will open the terminal. Wait for the terminal to come up.  
In it, type sudo nano /etc/nsconfig.  
This will open, using nano, the file containing the variables.  
  
After you are done editing the file and saving, you will need to reboot the virtual machine. To do that, type sudo reboot.  
Rebooting the machine will disconnect you and you will see an error message in the terminal. Just close the terminal.  
If you haven't done anything destructive, your Nightscout will keep going.  
  
If anything breaks, you can re-run [installation](./NS_Install).  
  
