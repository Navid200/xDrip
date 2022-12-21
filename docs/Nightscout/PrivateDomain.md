## Private Domain
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Google Cloud](./GoogleCloud.md) >> Private Domain  
  
You can use your private domain instead of a FreeDNS hostname.  
  
First, set up a FreeDNS hostname following the installation guide.  After completing installation and having a working setup, enter the following in a [terminal](./Terminal.md), where hostname is your private hostname.  
  
```
sudo certbot --nginx -d hostname --redirect --agree-tos --no-eff-email
```
  
Then, [reboot](./Restart.md) the server.  
