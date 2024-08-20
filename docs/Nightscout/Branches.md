---
layout: GCNS
---

## Google Cloud Nightscout branches
[Google Cloud Nightscout](./GoogleCloud.md) >> Branches  
  
Google Cloud Nightscout has two branches.  You can use either.  
This is not about Nightscout itself.  This is about the Google Cloud platform we use for running Nightscout.  
The development branch, vps-dev, is updated first and as a result, less tested.  You can access the latest updates sooner using this branch.  
The stable branch, vps-1, is updated after a new feature has been tested on the dev branch for a while.  It is updated by promoting a development branch release.  Therefore, it is more stable.  
<br/>  
  
---  
  
#### **Updates**  
When you [update the platform](./NS_SyncExecutables.md), you get the latest release in the branch you are using.  
<br/>  
  
---  
  
#### **Comparison**  
To see the differences between the two branches, you can view the [release notes](./GC_ReleaseNotes.md).  
<br/>  
  
---  
    
#### **Switch**  
If you are already using Google Cloud Nightscout and want to switch to the other branch, you need to run "Bootstrap" associated with the branch you want to switch to followed by "Install Nightscout phase 1" both from the [Google Cloud setup submenu](./Menu.md).  
You can then [restart the server](./Restart.md) to ensure that everything is working as expected.  If not, run "Install Nightscout Phase 2" as well.  It will ask you to change API_SECRET.  Just press ESC to keep it unchanged.  You will then be asked to enter credentials for your FreeDNS account.  Press ESC again to avoid changing the hostname.  
<br/>  
  
---  
   
#### **Which branch am I using?**  
Go to the status page.  The full GitHub path is shown on the status page.  The last section on the line is vps-1 or vps-dev for the stable or development branches respectively.  
![](./images/BranchFlag.png)  
<br/>  
  
---  
  
#### **Which branch should I use?**  
That's for you to decide.  
  
If you are confident with tech and are willing to contribute by testing, use the development branch.  
If you need a feature that is only available in the development branch, use the development branch.  
If you don't feel like using the new release, and the stable release gives you all you need, use the stable release.  
  
