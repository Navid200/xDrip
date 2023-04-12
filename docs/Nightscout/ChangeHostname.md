## How can I change hostname?
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Can I change hostname?  
  
You first need to log into your FreeDNS account and choose a domain and create a subdomain as described [here](./FreeDNS)  
If you have done this already and have created an alternative subdomain already, you can skip that step.  
  
Now assuming you already have a working Nightscout and all you want to do is to change the hostname, all you need to do is to run "Install Nightscout phase 2" from the Google Cloud setup submenu.  
  
You will be given the option to change your ASPI_SECRET.  You don't have to change it.  
Then, you will be asked to identify the subdomain you want to use.  Choose the one you want to switch to.  
After the setup completes, you will be asked to restart the server.  After you do that, your Nightscout will be on your new hostname.  
