## Linux/Nightscout install  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> [Linux/Nightscout install](./NS_Install)  
  
Now, we will install Linux on our [virtual machine](./NS_FreeTier), and Nightscout in it.  
  
If you have logged out, log back into Google and go to [https://console.cloud.google.com](https://console.cloud.google.com).  
Go to the dashboard (top left menu button &#8722;> Cloud Overview &#8722;> Dahsboard).  Select "Compute Engine" on the left.  
  
Click on "SSH" on the right side of the IP address on your Nightscout machine.  A terminal will appear shortly.  
![](./images/Terminal1.png)  

Copy and paste the following line into the terminal of your Nighscout virtual machine.  
**Caution:** Be very careful not to copy it into any other computer as it could overwrite the contents of the machine.  
  
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh \| bash  
   
Enter.  
  
If you get an error message that says EINTEGRITY ending with (0 bytes) and it hangs, just press CTRL and C keys at the same time to interrupt.  Then, re-run the curl bootstrap command.  You can use the up arrow on the terminal to go back to the previous command.  
  
After the installation is completed, you will be asked to login.  
![](./images/Terminal.png)  
To log in, use the email address and password you used to register for noip.com.  
  
Leave the update interval at 30.  
select N for running command during update.  
Enter email address "used for urgent renewal ...".  
Press A to accept the terms of service.  
Press N to decline sharing email address.  
Select 2 to redirect to secure https.  
  
You will be asked to change the API secret.  This is the password you will login to Nightscout with.  
Enter a password and take note.  
  
When you see the following, the install is complete and you can close the terminal by clicking on X at the top right corner of the terminal.  
![](./images/TermEnd.png)  
  
