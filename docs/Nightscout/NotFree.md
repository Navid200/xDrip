---
layout: GCNS
---

## Google Cloud cost  
[Google Cloud Nightscout](./GoogleCloud.md) >> Free Tier cost  

#### **Google Cloud Nightscout cost**  
Theoretically, if you don't reside in China or Australia, it will be free.  See limitations and requirements of a free tier machine below.  
However, due to unwanted traffic to China caused by crawlers in China, there is a cost of about $0.01 per month.  

The following image shows a summary of my costs over the past 11 months.  
![](./images/CostHistory.png)  

In February, I intentionally generated outgoing traffic as a test to see how much the cost will increase.  I copied the database to another Nightscout over and over again in order to create extra outgoing traffic.  
<br/>  
  
---  
  
#### **Free Trial**
When you first sign up for Google Cloud, you can enable a free trial program, and you should.  It gives you $300 for a 3-month period.  In that period, any cost will be charged against that credit.  However, you can see in your billing what the cost is.  
Therefore, if you live in China or Australia, you can clearly see what your cost will be, using this method, without having to pay anything.  If you find out that the cost will be unreasonable, you can delete the virtual machine, before your free trial period ends, and never have to pay for it.  
  
You will need a credit card to set up billing.  
<br/>  
  
---  
  
#### **Limitations and requirements of a free tier machine**  
  
When you follow the guide to install, you can clearly see, on the status page, if you have made any mistakes that violate the Free Tier requirements.  
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
<br/>  
  
---  

#### **Traffic to China or Australia**  

If you have a follower who is in Australia or China, the data traffic from your server to them will not be free.  
The estimated cost of traffic to Australia or China is less than $0.50 per GB per month.  
  
Any outgoing traffic to crawlers in China or Australia will also not be free.  This could result in a cost of about $0.01 per month for those not living in Australia or China.  
<br/>  

---  

#### **How could crawlers cause outgoing traffic without my API_SECRET?**  
There are crawlers everywhere scanning all IP addresses looking for vulnerabilities.  
When a crawler contacts your site, your Nightscout asks for either a read token or the API_SECRET. That request for a token or the API_SECRET is an outgoing traffic.  
Any such traffic to either China or Australia will result in a cost while traffic to all other geographic locations will only trigger a cost if the total traffic is greater than 1GB per month.  
  
<br/>  
  
---  
  
#### **Blocking traffic to China and Australia**  

We can block traffic to China in order to reduce the regular cost for those who don't live in China.  
However, this will complicate the installation scripts significantly resulting in higher verification times in maintenance.  

Therefore, it seems to be best to just put up with a cost of $0.01 per month.  
  
