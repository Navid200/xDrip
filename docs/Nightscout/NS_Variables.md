## Nightscout variables  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Variables  
  
After [setting up Nightscout](./NS_setup), you can edit your [Nightscout variables](https://github.com/nightscout/cgm-remote-monitor/blob/master/README.md#environment) if needed.  
  
To edit your Nightscout variables, open a [terminal](./Terminal).  
In the terminal, type and enter:  
  
<Mark>sudo nano /etc/nsconfig</Mark>  
  
This will open, using nano, the file containing the variables for edit.  
![](./images/nsconfig.png)  
To exit, press ctrl and X at the same time.  
To save, press ctrl and O at the same time.  It will offer the same name to save the file.  Press enter to approve.  Or, enter a different name to create a copy.  
  
After you are done editing and saving the file, you will need to reboot the virtual machine. To do that, type and enter:  
  
<Mark>sudo reboot</Mark>  
  
Rebooting the machine will disconnect you and you will see an error message in the terminal. Just close the terminal.  
If you haven't done anything destructive, your Nightscout will keep going.  
  
If anything breaks, you can edit the file again and change it back.  You can see the default values in the image above.  
  
Or, you can re-run [installation](./NS_Install).  
You will be offered the following two options:  
1- Attempt to reinstall this existing certificate  
2- Renew & replace the cert (limit ~5 per 7 days)  
  
Choose 1.  
<br/>  
  
---  
  
Let's add a variable to correctly reflect the database capacity.  I have set the disk space to 30GB.  Therefore, I have more than 20GB of space for the database.  
So, let's set it to 20000, which is reasonably close and leaves some margin.  
![](./images/dbsize.png)  
<br/>  
  
---  
  
For troubleshooting, if you want to temporarily allow anyone who has your hostname to be able to access your Nightscout, you need to change the following line:  
export AUTH_DEFAULT_ROLES="denied"  
Change denied to admin.  
This is an extremely unsafe setting and you should change it back as soon as possible.  
  
