## Free Tier virtual machine  
[xDrip](../../README.md) >> [Features](../Features_page) >> [Nightscout](../Nightscout_page) >> [Nightscout on Google Cloud](./GoogleCloud) >> Free Tier virtual machine  
  
Here, We will understand the limitations and requirements of a free tier machine.  Then, we will create a free virtual machine in our [Google project](./NS_GCProject).  
<br/>  
<br/>  
  
---  
  
### **Understanding the Constraints**  
You can have a look at the following, the [Google Cloud Free Program features, "Free Tier" section](https://cloud.google.com/free/docs/free-cloud-features#free-tier).  You will need to carefully select settings that qualify for the free tier category.  
The following snapshots show the current (September 4, 2022) limitations.  
  
![](./images/egrerss.png)  
The above constraint relates to where your followers are located.  
  
![](./images/Regions.png)  
The above three regions are the regions your virtual machine can be in, not where you or your followers have to be in.  
  
![](./images/MachineType.png)  
  
![](./images/DiskType.png)  
  
![](./images/DiskVer.png)  
  
You can use the [calculator](https://cloud.google.com/products/calculator) to confirm the price.  
![](./images/Estimate.png)  
<br/>  
<br/>  
  
---    
  
### **Creating a virtual machine**  
Let's create the machine now.  
Go to dashboard.  Select "Compute Engine".  Select the "VM instances" tab in the left pane if it's not already selected.  
Select "Create Instance".  
![](./images/CreateInstance.png)  
  
Set name to whatever you like.    
  
Set machine type to "e2-micro".  
Select a region that qualifies for free tier.  
Pay special attention to the settings marked with stars (in the images below) as they can affect cost and you will not be able to change them after you create the machine unless you delete and recreate the machine.  
![](./images/vm2.png)  
  
Edit "Boot Disk" and modify as shown below.  Click on "Select".  
Pay attention to the marked settings as they can affect cost and you will not be able to change them later.  
![](./images/Disk2.png)  
  
Under firewall, enable both http and https.  
![](./images/Firewall2.png)  
  
You will not be able to change any of this after you create.  The only way to change is to delete the machine and create a new one.  Therefore, before clicking on create, make sure the conditions listed in the following table are satisfied to ensure you will not be charged.  
  
| Parameters | Requirements |  
| ---------- | ------------ |  
| Machine Region    | Oregon, Iowa or South Carolina |  
| Machine Type | e2-micro |  
| Disk type    | persistent disk |  
| Disk size   |  Less than or equal to 30GB |  
  
Click on "Create" to create the virtual machine.  Google will now bring up your virtual machine.  Wait for it to come up.  When the external IP column appears, you should be good to go.  
  
