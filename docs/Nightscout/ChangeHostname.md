---
layout: GCNS
---

## How can I change the hostname?
[Google Cloud Nightscout](./GoogleCloud.md) >> Can I change the hostname?  
  
If you already have a working Nightscout setup following our [instructions](./GoogleCloud.md) and simply want to change the hostname, this guide explains how to do it.  
  
1- Create a New Subdomain in FreeDNS  
Log in to your FreeDNS account, choose a domain, and create a new subdomain as described [here](./FreeDNS).  
If you’ve already created an alternative subdomain, you can skip this step.  
  
2- Run "Install Nightscout Phase 2"  
Open the Google Cloud setup submenu and select `Install Nightscout Phase 2`.  
  
3- Update Settings During Setup  
- You’ll be given the option to change your API_SECRET, but this is not mandatory.  
- Next, you’ll be prompted to identify the subdomain you want to use. Select the new subdomain.  
  
4- Restart the Server  
After the setup completes, you’ll be asked to restart the server. Once restarted, your Nightscout will be accessible using the new hostname.  
  
5- Update Uploader and Followers  
Update your uploader to use the new hostname. Additionally, notify your followers and provide tokens to use the updated hostname.  
  
Note:  
Your previous hostname may remain active for a while but will eventually stop working. Ensure all devices and users are transitioned to the new hostname promptly.  
  
  
