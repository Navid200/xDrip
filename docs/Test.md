---
layout: GCNS
---

## Test  
This page is for testing only.  Please do not follow the content as it will be out of context.
  
<br/>  
<br/>  

Now, we will update Linux on our [virtual machine](./NS_FreeTier.md), and install Nightscout on it.  You must have [FreeDNS](./FreeDNS.md) and [Google Project](./NS_GCProject.md) set up already to proceed.  
<br/>  
  
---  
  
**Bootstrap**    
Open a [terminal](./Terminal.md).  
  
Copy and paste the following highlighted line into the terminal of your Nighscout virtual machine and enter.  
**Caution:** Be very careful not to copy it into any other computer as it could overwrite the contents of the machine.
  
<input type="text" value="curl https://raw.githubusercontent.com/jamorham/nightscout-vps/vps-1/bootstrap.sh | bash" readonly id="myInputText" style="border:none; color:#101010; background-color:#ededed; width:100%; font-size:15px">  
<button onclick="FunctionCopyText()" style="border: 1px solid #0066ff; color:#f0f0f0; background: linear-gradient(#0066ff, #0066ff); font-size:14px; background-color:#0066ff; font-weight:400; border-radius: 2px; margin-left:70px; margin-top:8px; padding:4px 12px; display:inline-block; box-shadow: inset 0px 1px 0px rgba(255,255,255,.3), 0px 1px 5px rgba(0,0,0,.7); :hover ">Copy</button>
  
<br/>  
  
You will be presented with a notification.  To acknowledge, you need to select OK.  However, you will not be able to use the mouse.  Just press enter.  [This](./HowToMenu.md) explains how the menus work.  
It will take a few minutes to complete a preliminary installation step.  You will then see a note or the status page.  
The following table shows what you may see depending on different choices you can make.  
  
| Incorrect choice | Clue | Outcome |  
| :--------------: | :----------: | :-----: |  
| Nothing | [<img src="./images/AllGood_Icon.png">](./images/AllGood.png) | All good ✅ |  
| Region | [<img src="./images/AllGood_Icon.png">](./images/Region.png) | Not free ❌ |  
| Machine type | [<img src="./images/AllGood_Icon.png">](./images/NoMicro.png) | Not free ❌ |  
| Disk type | [<img src="./images/AllGood_Icon.png">](./images/NotStandard.png) | Not free ❌ |  
