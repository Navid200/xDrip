## Invoice for egress to China
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >>  Invoice for traffic to China  
  
I thought this was supposed to be free.  I followed all the instructions when setting up my site.  
Yet, I have been invoiced a non-zero value.  Why?  
  
There are crawlers everywhere scanning all IP addresses looking for vulnerabilities.  
When a crawler contacts your site, your Nightscout asks for either a read token or the API_SECRET.  That request for a token or the API_SECRET is an outgoing traffic.  
Any such traffic to either China or Australia will result in a cost while traffic to all other geographic locations will only trigger a cost if the total traffic is greater than 1GB per month.  

You can use a [cost table](./CostTable.md) to confirm that is the reason for your non-zero invoice.  If you see an unexpected cost, please [tell us](https://github.com/NightscoutFoundation/xDrip/discussions).  
  
We are working on adding switches to let you block traffic completely to China or Australia.  Hopefully, it will be available soon.     
