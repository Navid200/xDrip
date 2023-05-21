---
layout: GCNS
title: "How can I change the hostname?"
permalink: "/docs/Nightscout/ChangeHostname.html"
---

## How can I change the hostname?
[Google Cloud Nightscout](./GoogleCloud.md) >> Can I change the hostname?  
  
Assuming you already have a working Nightscout following our [instructions](./GoogleCloud.md), and all you want to do is to change the hostname, this page explains how to do it.  

You first need to log into your FreeDNS account and choose a domain and create a new subdomain as described [here](./FreeDNS).  
If you have done this already and have created an alternative subdomain as well, you can skip this step.  
  
Now, run "Install Nightscout phase 2" from the Google Cloud setup submenu.  
  
You will be given the option to change your API_SECRET.  You don't have to change it.  
Then, you will be asked to identify the subdomain you want to use.  Choose the one you want to switch to.  
After the setup completes, you will be asked to restart the server.  After you do that, your Nightscout will be on your new hostname.  
