---
layout: GCNS
title: "Update Google Cloud Nightscout platform"
permalink: "/docs/Nightscout/NS_SyncExecutables.html"
---

## Update Google Cloud Nightscout platform
[Google Cloud Nightscout](./GoogleCloud.md) >> Update to latest release   
  
We update the Google Cloud Nightscout platform in order to add functionality and fix issues.  This is how you can update the menus, commands and packages of your virtual machine to the latest.  This is not about updating Nightscout.  If you want to update Nightscout, please have a look at [this](./update_nightscout.md).  
If you have not completed the [installation](./GoogleCloud.md), please complete it first.  
  
Open a [terminal](./Terminal.md).  A [menu](./Menu.md) should come up automatically.  Select "Update platform" from the Google Cloud setup submenu.  You can then check the status page to confirm that your setup has been [updated](./GC_ReleaseNotes.md).  
  
If you created your setup a long time ago and never updated it, you may need to go through a few more steps, explained in the following sections, to complete the update.  
<br/>  
<br/>  
  
---  
  
#### **If you created your setup a long time ago**  
Our platform has evolved significantly since it was first released.  You don't need to start from scratch.  But, you may need to go through a few steps in order to update depending on how long ago you created your setup.  After you update, there will be no difference between your setup and one that has just been created.  

If you are still using noip.com, you will be asked, below, to create a FreeDNS account and a new hostname in order to switch from noip.com to FreeDNS.  
  
If a menu does not open when you open a terminal, please skip to the section titled "Bootstrap" below.  
  
If a menu opens when you open a terminal, from the menu, or Google Cloud setup submenu, select "Update scripts" or "Update platform".  After completion and closing the terminal and opening a new one, if a menu does not automatically open up, please skip to the section titled "Bootstrap" below.  
  
View the Status page by selecting "Status".  If it looks like [this](./images/Status.png), the platform has updated to the latest.  If not, please update platform from the Google Cloud setup submenu again.  
  
If the status page still looks different, for example, if the username, repository name and branch name are blank, as shown below, please proceed to the section titled "Bootstrap" below.  
![](./images/NoRepoStat.png)  
<br/>  
  
---  
  
#### **Bootstrap**  
Open a [terminal](./Terminal.md).  If a menu opens, select "Bootstrap" from the Google Cloud submenu.  
  
If no menu opens after opening a terminal, copy the entire highlighted text below and paste into the terminal.  Press enter.  
  
```  
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash
```  
  
You will see the following note:  
![](./images/BootstrapConfirm.png)  
Press enter to proceed.  After bootstrap is complete, you will go to the status page.  
You must now run "Install Nightscout phase 1" from the Google Cloud setup submenu.  
Please proceed to the following sections.  
<br/>  
  
---  
  
#### **Missing packages**
If you see "Missing packages" on the status page as shown below, please run "Update platform" from the Google Cloud setup submenu again.  
![](./images/MissingPackages.png)  
<br/>  
  
---  
  
#### **Missing node_modules**
If you see "Missing node_modules" on the status page as shown below, please run "Install Nightscout phase 1" from the Google Cloud setup submenu.  
![](./images/MissNodeModules.png)  
<br/>  
  
---  
  
#### **No Hostname**
If you are still using noip.com, after you run bootstrap and run "Install Nightscout phase 1", you will see "No hostname" on the status page as shown below.  
![](./images/NoHostname.png)  
  
To switch to FreeDNS, please follow the instructions [here](./FreeDNS.md) to create a FreeDNS account and create a new hostname.  
Then, please run "Install Nightscout phase 2" from the Google Cloud setup submenu.  
<br/>  
  
---  
  
#### **Startup dependence on FreeDNS site**
If you see "Startup Dependence on FreeDNS" on the status page as shown below, please run "Install Nightscout phase 2".  
![](./images/StartFreeDNS_dependence.png)  
  
You will be asked if you want to change API_SECRET.  Just press Escape unless you want to change it.  
You will be asked for your FreeDNS user ID and password.  See [this](FreeDNS_userID.md) if you don't remember your user ID.  Enter those.  
You will be asked to identify the subdomain to use.  Enter the first few characters of the same subdomain you have been using unless you want to change it.  
