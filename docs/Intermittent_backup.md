## xDrip Dexcom Intermittent Connectivity  
  
xDrip needs to wake up and scan once every 5 minutes in order to maintain connectivity and provide consistent readings.  
New versions of Android have features to extend the phone battery run time.  This is done by minimizing background activity.  
The phone manufacturers also include their methods for accomplishing the same.  
This behavior interferes with proper operation of xDrip.  As a result, when you first install xDrip on Android 10 and above, it is almost guaranteed it will have connectivity issues.  
It is hard to provide instructions for all available brands and models of handsets.  Please use what follows as a method for tweaking your device to find the best settings.  

1. If you are using an xDrip release older than the current stable release, please [update](./Updates.md).  
2. Start from the recommended settings:  
[G5](./G5-Recommended-Settings.md)  
[G6](./G6-Recommended-Settings.md)    
3. Disable any battery optimization/adaptation settings you can find under Android Settings -> Battery.  
4. If the problem persists, please update to the Nightly release: https://github.com/NightscoutFoundation/xDrip/releases/tag/2022.06.09  
<!--- 5. If the problem persists, and you are on Android 10 or higher, disable "Avoid Scanning" on the G5/G6 Debug Settings page. You can also disable "Minimize Scanning".  Please note that those two settings are intentionally enabled on Android 10 and above because it's believed to give the best results.  But, it may vary on some devices.  
6. If the problem persists, re-enable "Minimize Scanning" and "Avoid Scanning" on Android 10 or higher.  
Does it keep unpairing?  Do you get frequent pair requests?  If yes, disable "Allow OB1 unbonding".  --->  
