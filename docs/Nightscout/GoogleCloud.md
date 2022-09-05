## Nightscout on Google Cloud  
[xDrip](../../README.md) >> [xDrip & Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md)  
  
This is still a draft and is being tested.  
  
Thanks to jamorham, the current xDrip head developer, for the idea, putting it together, implementing the installer, and testing everything.  
  
You will need a credit card to set up billing.  The plan is to have no billing.  But, we are still testing to confirm that it will be free.  Regardless, a credit card is required.  
  
The screenshots have been created from a computerr.  It may be difficult to follow on a mobile device.  
It will take a while (about 90 minutes) to go through the entire setup.  There are suggested break periods.  
  
---  
  
Go to [https://www.noip.com](https://www.noip.com).  
![](./images/noip.png)  
<br/>  
  
Sign up if you don't have an account.  
You need to enter your email address, choose a password, and choose a hostname.  Make a note of all.  
![](./images/noipSignup.png)  
<br/>  
  
Select "Free Sign Up" instead of "Get Enhanced".  
![](./images/noipFree.png)  
<br/>  
  
Wait for the email and "Confirm Account" before you proceed.  This is a good time for a break.  
  
---  
  
You need a Google account to proceed.  Create one if you don't have one.  
Go to  [https://console.cloud.google.com](https://console.cloud.google.com) and log in with your Google account.  
![](./images/GoogleCloud.png)  
<br/>  
  
Create a project.  
![](./images/NewProject.png)  
<br/>  
  
Select your new project.  
![](./images/SelectProject.png)  
<br/>  
  
Go to dashboard.  Select "Compute Engine".  
![](./images/Dash.png)  
<br/>  
  
Enable.  
![](./images/Enable.png)  
<br/>  

Enable billing.  
![](./images/EnableBilling.png)  
<br/>  
  
Go to https://console.cloud.google.com/
Enable the engine again.  It will ask you to enable billing again.  But, this time, select the billing account you just created.  
Enable billing for your new project.  
![](./images/BillingforProject.png)  
<br/>  
  
You will be asked to select your country and describe your organization.  Select "Personal project".  
![](./images/Country.png)  
<br/>  
  
Enter billing details.  Select "Start free trial".  This is a good time for a break.   
  
---  
  
You can have a look at the following, the [Google Cloud Free Program features, "Free Tier" section](https://cloud.google.com/free/docs/free-cloud-features#free-tier).  You will be setting up a virtual machine next.  You will need to carefully select settings that qualify for the free tier category.  
The following snapshots show the current (September 4, 2022) limitations.  
![](./images/egrerss.png)  

  
Standard disk  
Minimal  
Create  
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash  
If you get an error message that says EINTEGRITY ending with (0 bytes) and it hangs. Just press CTRL C keys at the same time and re-run the curl bootstrap command.  
You will be asked to login.  Use the email address and password you use for noip.com.  
Leave the update interval at 30.  
select N for running command during update.  
Enter email address.  
Press A to accept the terms of service.  
Press N to decline sharing email address.  
Select 2 to redirect to secure https.  
The API secret will be your Nightscout password.  Enter a password and take note.  
