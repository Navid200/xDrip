## Free Tier  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Free Tier  
  
You will need a credit card to set up billing. This will be free as long as you don’t have too much outgoing traffic (more than 1GB per month) from your Google Cloud server. Regardless, a credit card is required.  
  
Next, We will understand the limitations and requirements of a free tier machine.  
  
You can have a look at the details under the "Free Tier" heading [here](https://cloud.google.com/free/docs/free-cloud-features#free-tier).  
When you create your virtual machine, you will need to carefully select settings that qualify for the free tier category.  
  
The following snapshots show the current (November 11, 2022) limitations.  
  
![](./images/FreeTier.png)  
  
![](./images/Regions.png)  
The above three regions are the regions your virtual machine can be in, not where you or your followers have to be in.  
  
![](./images/egrerss.png)  
The above constraint relates to where your followers are located.  
  
You can use the [calculator](https://cloud.google.com/products/calculator) to confirm the price.  
![](./images/Estimate.png)  
  
If you have a follower who is in Australia or China, the data traffic from your server to them will not qualify for Free Tier.  
Any outgoing traffic to crawlers in China or Australia will also not be free.  This could result in a cost of about 1-3c per month.  We will hopefully add a switch to block traffic to China (enabled by default).  
