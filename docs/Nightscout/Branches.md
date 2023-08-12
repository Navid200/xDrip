---
layout: GCNS
---

## Google Cloud Nightscout branches
[Google Cloud Nightscout](./GoogleCloud.md) >> Branches  
  
Google Cloud Nightscout has two branches.  You can use either.  
This is not about Nightscout itself.  This is about the Google Cloud platform we use for running Nightscout.  
The development branch, vps-dev, is updated more frequently and as a result, less tested.  You can access the latest updates the fastest using this branch.  
The stable branch, vps-1, is updated less often.  It is updated by promoting a development branch release.  Therefore, it is more stable.  
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
If you are already using Google Cloud Nightscout and want to switch to the other branch, all you need to do is to run the bootstrap associated with the branch followed by running "Install Nightscout phase 1" from the Google Cloud setup submenu.  
You can then restart the server to ensure that everything is working as expected.  
<br/>  
  
---  
  
#### **Stable branch bootstrap**  
  
<input type="text" value="curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash" readonly id="myInputText" style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">  
<button onclick="FunctionCopyText()" style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; background-color:#0066ff; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7); :hover ">Copy</button>  
  
<br/>  
  
---  
  
#### **Development branch bootstrap**  
  
<input type="text" value="curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-dev/bootstrap.sh | bash" readonly id="myInputText" style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">  
<buttondev onclick="FunctionCopyText()" style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; background-color:#0066ff; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7); :hover ">Copy</buttondev>  
  
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
  
