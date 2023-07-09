---
layout: GCNS
permalink: "/docs/Nightscout/502BadGateway.html"
---

## 502 Bad Gateway
[Google Cloud Nightscout](./GoogleCloud.md) >> 502 Bad Gateway  
  
Do you get this error when you attempt to access your Nightscout in a browser?  
<br/>  
  
![](./images/502BadGateway.png)  
<br/>  
<br/>  
  
This could have been caused by having run bootstrap and not having followed by executing Install Nightscout phase 1.  
Please check the [status page](./Status.md).  If it shows "Missing node_modules" in red, you need to run "Install Nightscout phase 1" from the Google Cloud setup submenu.  
Then, [restart the server](./Restart.md) to resolve the issue.   
  
After restast, if the problem persists, please [contact us](./GCNS_Support.md).  
  
  
