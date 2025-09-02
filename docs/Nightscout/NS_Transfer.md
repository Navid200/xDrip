---
title: "Transfer Data from Another Nightscout Instance"
description: "Learn how to transfer past glucose readings from another Nightscout repository—such as Heroku—to your Google Cloud Nightscout setup. Includes documentation and help for data migration."
layout: GCNS
---

# Data transfer from another Nightscout
[Google Cloud Nightscout](./GoogleCloud.md) >> Data Transfer  
  
This is how you can transfer your past readings from another repository, for example, from Heroku.    
  
Open a [terminal](./Terminal.md).  
Select "Copy data from another Nightscout" from the Data submenu and Enter.  
  
You will be asked for site name.  Enter the URL you use to access the source Nightscout site.  For example, https://UserId.herokuapp.com of Heroku.  
You will be asked for a read token.  If the source Nightscout is open to the world, not recommended, you can just hit enter.  If it's not, enter a read token.  You can see [here](./NS_Followers.md) how to create and use tokens.  
  
After you enter the token, the transfer will start.  
![](./images/Transfer.png)  
  
It may take a while to complete depending on how much data you have in the previous repository.    
  
