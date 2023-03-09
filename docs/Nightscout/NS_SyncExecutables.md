## Update Google Cloud Nightscout platform
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Update to latest release   
  
This is how you can update the menus, commands and packages on your virtual machine.  This is not about updating Nightscout.  If you want to update Nightscout, please have a look at [this](./update_nightscout.md).  
  
Are you still using noip.com?  If yes, you will need to go through [this](./FreeDNS.md) to create a FreeDNS account and a new hostname.  You will need those when you run "Install Nightscout phase 2".  
  
Open a [terminal](./Terminal.md).  A [menu](./Menu.md) should come up automatically.  If not, you have a very old version of our platform.  To update, please skip to the section titled "Bootstrap" below.  
  
From the menu, or Google Cloud setup submenu, select "Update scripts" or "Update platform".  After completion and closing the terminal and opening a new one, if a menu does not automatically open up, please skip to the section titled "Bootstrap" below.  
  
If the menu opens, view the Status page by selecting "Status".  If it looks like [this](./images/Status.png), the platform has updated to the latest.  If not, please update platform from the Google Cloud setup submenu again.  
  
If the status page still looks different, for example, if the username, repository name and branch name are blank, as shown below, please proceed to Bootstrap explained below.  
![](./images/NoRepoStat.png)  
<br/>  
  
---  
  
#### **Bootstrap**  
Open a [terminal](./Terminal.md).  If a menu opens, select "Bootstrap" from the Google Cloud submenu.  
  
If no menu opens after opening a terminal, using SSH, Copy the entire highlighted text below and paste into the terminal.  Press enter.  
  
```  
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash
```  
<br/>  
  
You will see the following note:  
![](./images/BootstrapConfirm.png)  
Press enter to proceed.  After bootstrap is complete, you will go to the status page.  
  
**You must now run "Install Nightscout phase 1" from the Google Cloud setup submenu.**  
<br/>  
  
Please proceed to the next section.  
<br/>  
  
---  
  
#### **Missing packages**
If you see "Missing packages" on the status page as shown below, please run "Update platform" from the Google Cloud setup submenu again.  
![](./images/MissingPackages.png)  
<br/>  
  
---  
  
#### **No Hostname**    
If after you run bootstrap and "Install Nightscout phase 1", you see "No hostname" on the status page as shown below, please run "Install Nightscout phase 2" from the Google Cloud setup submenu.  
![](./images/NoHostname.png)  
  
