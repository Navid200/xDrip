---
layout: GCNS
permalink: "/docs/Nightscout/FreeDNS.html"
---

## Free DNS
[Google Cloud Nightscout](./GoogleCloud.md) >> FreeDNS  

Follow these instructions in order to use FreeDNS, which you will need in order to set up Google Cloud Nightscout.  
After everything is set up and working, you will need to log into your FreeDNS account at least once every 6 months, or when you receive a reminder email from them, as explained [here](./FreeDNS_Min_Login.md).  
<br/>  
  
---  

#### **Creating an account**  
Go to FreeDNS site: [https://freedns.afraid.org/](https://freedns.afraid.org/)  
Click on Sign up Free, and sign up.  Make sure to choose a user ID excluding capital letters.  
If you include capital letters in your user ID, FreeDNS will convert them all to lowercase.  
Please remember your password.  
  
Send activation email.  
![](./images/FreeDNS1.png)  
Open email and activate your account.  
**<span style="color:red">Set up your email client not to filter the email, from FreeDNS, as junk/spam.  You will receive an email from them once every 5.5 months as a reminder to log into your FreeDNS account to keep your account active.</span>**  
  
To log in, click on "Main Menu" under "For Members".  
After logging in, you can see your user ID at the top right corner.  
![](./images/FD_userID.png)  
  
You will need your user ID and password later to use the FreeDNS Setup utility.  Please remember your user password.  
<br/>  

---  

#### **Choosing a domain**    
Go to [Subdomains](https://freedns.afraid.org/subdomain/).  
Add.  
  
Under "Domain", you have 7 options.  Instead of using one of those, it may be better to do what is suggested [here](./FD_Domains.md) if you cannot reach your site at work or school.  
If you don't experience any issues at work or school, using one of the 7 main domains may be better to avoid the possibility of the domain going down at some point in the future.  In that case, you can resolve the problem by creating a new subdomain and [switching to it](./ChangeHostname.md).  
  
After having selected a public domain, please proceed with creating a subdomain.  
<br/>  
  
---  
  
#### **Creating a subdomain**    
Enter a subdomain name.  Under the image, enter text (capital) to capture the image you see.  You can change the image if it's not clear.  
Save.  
![](./images/FreeDNS2.png)  
<br/>  
  
Now, if you go to [Subdomains](https://freedns.afraid.org/subdomain/), you will see your new hostname:  
![](./images/FD_hostname.png)  
That is what you will be using to access your Nightscout in a web browser.  
<br/>  
<br/>  
 
