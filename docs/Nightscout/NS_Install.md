## Linux/Nightscout install  
[xDrip](../../README.md) >> [xDrip & Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> [Linux/Nightscout install](./NS_Install)  
  
After the external IP address column is populated, click on "SSH" on the right side of the IP address.  A terminal will appear shortly.  
![](./images/Terminal1.png)  

Copy and paste the following line into the terminal.  Be very careful not to copy it into any other machine as it will overwrite the contents of the machine.  
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
  
