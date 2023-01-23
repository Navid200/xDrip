## Update Google Cloud Nightscout platform
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Update to latest release  
  
**<span style="color:red">Please be patient until we resolve an issue with the update strategy.</span>**  
Please wait for the above note to be removed, in the next day or two, before you proceed with updating.  
January 23, 2023  
  
This is how you can update your virtual machine with the latest menus, commands and packages.  
  
Open a [terminal](./Terminal.md).  A [menu](./Menu.md) should come up automatically.  If not, you have a very old version of our platform.  To update, please skip to the section titled "Bootstrap" below.  
  
From the menu, select "Update scripts".  After completion and closing the terminal and opening a new one, if a menu does not automatically open up, please skip to the section titled "Bootstrap" below.  
  
If the menu opens, view the Status page by selecting "Status".  If it looks like [this](./images/Status.png), the platform has updated to the latest.  If not, please update platform from the Google Cloud setup submenu.  
  
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
Press enter to proceed.  After bootstrap is complete, go to the status page.  Please see the next section.  
<br/>  
  
---  
  
#### **Missing packages**
If you see "Missing packages" on the status page as shown below, please run "Update platform" from the Google Cloud setup submenu again.  
![](./images/MissingPackages.png)  
  
  
