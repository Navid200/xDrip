---
layout: GCNS
title: "Nightscout Google Cloud: Changing Hostname"
description: "Step-by-step instructions to change your Nightscout hostname on Google Cloud: create a FreeDNS subdomain, run Install Nightscout Phase 2, restart the server, and update your uploader and followers."
---

# How can I change the hostname?
[Google Cloud Nightscout](./GoogleCloud.md) >> Can I change the hostname?  
  
If you already have a working Nightscout setup following our [instructions](./GoogleCloud.md) and simply want to change the hostname, this guide explains how to do it.  
  
**1- Create a New Subdomain in FreeDNS**  
Log in to your FreeDNS account, choose a domain, and create a new subdomain as described [here](./FreeDNS.md).  
If you’ve already created an alternative subdomain, you can skip this step.  
  
**2- Run "Install Nightscout Phase 2"**  
Open the Google Cloud setup submenu and select `Install Nightscout Phase 2`.  
  
**3- Update Settings During Setup**  
- You’ll be given the option to change your API_SECRET, but this step is optional.  
- If you have multiple FreeDNS accounts, enter the user ID and password for the account with the new subdomain.  
- You’ll then be prompted to select the subdomain you want to use. Choose the new subdomain.  
  
**4- Restart the Server**  
Once the setup is complete, you’ll be asked to restart the server. After restarting, your Nightscout will be accessible using the new hostname.  
  
**5- Update Uploader and Followers**  
Update your uploader to use the new hostname. Additionally, notify your followers and provide them with the necessary tokens to access the updated hostname.  
<br/>  
  
**Note:**  
Your previous hostname may remain active temporarily but will eventually stop working. Make sure all devices and users are updated to use the new hostname as soon as possible.  
  
  
