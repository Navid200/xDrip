## Wear OS Instructions  
  
You can see a copy of instructions provided by jamorham in a post in an issue here:  
  
<br/>  

jamorham commented on Apr 13, 2021  

I've added this in the April 13th Nightly which is available now.  

So for the updated version the instructions are as follows:  

- Install the latest nightly xDrip WearOs component to the watch, eg using Wear Installer app, downgrading play store or via adb manually.
- Make sure developer mode is enabled on the Watch  
- Make sure that wifi adb debugging is enabled
- Go to xDrip Prefs on the Watch, scroll till you see Force Update - tap it
- It should check ADB, and ask for authorization, select Always Allow
- Probably it will tell you ADB isn't working due to needing the authorization
- Go in to xDrip Prefs again on the watch and click Force Update
- This time it should say ADB looks good and then ask xDrip phone app to send the latest version
- This should download and then xDrip will install itself.  
  
Because the versions are currently identical then this wont really do much, but this process can be used to force xDrip to send the latest version in future but it should also automatically detect version changes and update in the background. For fun you could install the latest nightly on the watch then downgrade your phone and check that the update process then downgrades your watch app version. The version information should be displayed next to the Force Update text.  
  
The idea is that you no longer need to use Wear Installer or whatever method you used to install the first time around (eg PC based adb or downgrading play store) as xDrip can update itself.  
  
<br/>
  
You can find the original [here](https://github.com/NightscoutFoundation/xDrip/issues/1648#issuecomment-818600901).  
