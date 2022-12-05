## Update commands & menus
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Update to latest release  
  
This is how you can update your virtual machine with the latest menus and commands.  
  
Open a [terminal](./Terminal.md).  
A [menu](./Menu.md) should come up automatically.  If not, you have a very old version of our platform.  To update, please proceed to the section titled "Bootstrap" below.  
  
From the menu, select "Update scripts".  After completion and closing the terminal and opening a new one, if a menu does not automatically open up, please proceed to the section titled "Bootstrap" below.  
<br/>  
  
---  
  
#### **Bootstrap**  
Open a [terminal](./Terminal.md).  If a menu opens, close it by choosing "Exit to shell".  Copy the entire highlighted text below and paste into the terminal.  Hit enter.  
  
<mark style="background-color: #eFdFef">curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash</Mark>  
  
You will see the following note:  
![](./images/BootstrapConfirm.png)  
Press enter to proceed.  After bootstrap is complete, your platform will be updated to the latest release.  
  
