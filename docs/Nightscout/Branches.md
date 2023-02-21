## Nightscout on Google Cloud branches
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [Nightscout](../Nightscout_page.md) >> [Nightscout on Google Cloud](./GoogleCloud.md) >> Branches  
  
Nightscout on Google Cloud has two branches that you can choose from to use.  
This is not about Nightscout itself.  This is about the Google Cloud platform we use for running Nightscout.  
The development branch, vps-dev, is updated more frequently and as a result, less tested.  You can access the latest updates the fastest using this branch.  
The stable branch, vps-1, is updated less often.  It is updated by promoting a development branch release to it.  Therefore, it is more stable.  
<br/>  
  
---  
  
#### **Updates**  
When you [update the platform](./NS_SyncExecutables.md), you get the latest release in the branch you are using.  
<br/>  
  
---  
  
#### **Comparison**  
To see the differences between the two branches, you can use the [release notes](./GC_ReleaseNotes.md).  
<br/>  
  
---  
    
#### **Switch**  
If you are already using Nightscout on Google Cloud and you want to switch to the other branch, all you need to do is to run the bootstrap associated with the branch followed by running "Install Nightscout phase 1" from the Google Cloud setup submenu.  
You can then restart the server to ensure that everything is working as expected.  
<br/>  
  
---  
  
#### **Stable branch bootstrap**  
````
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash
````  
<br/>  
  
---  
  
#### **Development branch bootstrap**   
````
curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-dev/bootstrap.sh | bash
````  
<br/>  
  
---  
  
#### **Which branch am I using?**  
Go to the status page.  The full GitHub path is shown on the status page.  The last section on the line is vps-1 or vps-dev for the stable or development branches respectively.  
![](./images/BranchFlag.png)  
  
  
