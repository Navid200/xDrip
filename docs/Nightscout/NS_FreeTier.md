## Free Tier virtual machine  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> [Free Tier virtual machine](./NS_FreeTier)  
  
You can have a look at the following, the [Google Cloud Free Program features, "Free Tier" section](https://cloud.google.com/free/docs/free-cloud-features#free-tier).  You will be setting up a virtual machine next.  You will need to carefully select settings that qualify for the free tier category.  
The following snapshots show the current (September 4, 2022) limitations.  
  
![](./images/egrerss.png)  
  
![](./images/Regions.png)  
The above three regions are the regions your virtual machine can be in, not where you or your followers have to be in.  
  
![](./images/MachineType.png)  
  
![](./images/DiskType.png)  
  
![](./images/DiskVer.png)  
  
You can use the [calculator](https://cloud.google.com/products/calculator) to confirm the price.  
![](./images/Estimate.png)  
  
Go to dashboard.  Select "Compute Engine".  Select the "VM instances" tab in the left pane if it's not already selected.  
Select "Create Instance".  
![](./images/CreateInstance.png)  
  
Set name to "nightscout".  
Set machine type to "e2-micro".  
Select a region that qualifies for free tier.  
  
![](./images/vm2.png)  
  
Edit "Boot Disk" and modify as shown below.  Click on "Select".  
![](./images/Disk2.png)  
  
Under firewall, enable both http and https.  
![](./images/Firewall2.png)  
  
Click on "Create" to create the virtual machine.  Google will now bring up your virtual machine.  Wait for it to come up.  When the external IP column appears, you should be good to go.  
  
[Delete a VM](./images/DeleteVM.png)  
  
