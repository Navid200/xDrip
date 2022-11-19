## Free DNS
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> FreeDNS  

Follow these instructions in order to use FreeDNS, which you will need in order to set up Nightscout on Google Cloud.  
<br/>  
  
---  
**FreeDNS Site**  
Go to FreeDNS site: [https://freedns.afraid.org/](https://freedns.afraid.org/)  
Click on Sign up Free, and sign up.  
Send activation email.  
![](./images/FreeDNS1.png)  
Open email and activate your account.  
  
Click on Main Menu to log in.  
After logging in, you can see your user ID at the top right corner.  
![](./images/FD_userID.png)  
You will need your user ID and password later to use the FreeDNS Setup utility.  
  
Go to [Subdomains](https://freedns.afraid.org/subdomain/).  
Add.  
Enter a subdomain name.  Pick a public domain.  Under the image, enter text (capital) to capture the image you see.  You can change the image if it's not clear.  
Save.  
![](./images/FreeDNS2.png)  
<br/>  
  
Now, if you go to [Subdomains](https://freedns.afraid.org/subdomain/), you will see your new subdomain:  
![](./images/FD_hostname.png)  
That is what you will be using to access your Nightscout in a web browser.  
<br/>  
<br/>  
  
---  
**FreeDNS Utility**  
Open a [terminal](./Terminal.md) on the virtual machine.  
Select "FreeDNS Setup" from the main menu.  
If you don't see it on the main menu, select Update scripts and enter.  After it completes, close the window and open a new one.  Now, you will have FreeDNS Setup on the menu.  
  
You will be asked for your user ID and password.  Enter those.  
After you have provided what the script needs, it will perform the necessary edits to enable FreeDNS.  Please be patient allowing the script to complete.  It may take up to 10 minutes.  

Only if you see a disconnect message, just close the window, open a new one and rerun the utility.  Otherwise, please allow the script to continue to run until it completes even if you see errors on screen even in red.  

When the process completes, Nightscout will be running, and accessible through your Free DNS subdomain (hostname).  
 
