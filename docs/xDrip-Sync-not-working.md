## xDrip Sync not working
[xDrip](../README.md) >> [Troubleshooting](./Troubleshooting_page.md) >> xDrip Sync not working  
  
As of July 2024, we have made some changes to xDrip sync due to updates in Google Cloud messaging. To ensure proper functionality, please update both the master and follower to an xDrip release from [2024.07.23](https://github.com/NightscoutFoundation/xDrip/releases/tag/2024.07.23) or later.  
  
We are aware that the sensor expiry, battery percentage, and other details are no longer displayed on the follower. These issues will be addressed, but for now, it is crucial to use the new version to obtain readings. Once this is resolved, we will work on fixing the remaining issues.  
  
Thank you for your patience.  
  
If updating both the master and follower does not resolve the issue and you are unable to get readings, please [report the problem](./Contact.md). In the meantime, you may use [Nightscout](./Nightscout_page.md) as an alternative.
  
<br>  
  
---  
  
The instructions for setting up xDrip Sync can be found [here](./xDripSync.md).  
Ensure that "Disable all sync features" is disabled (no check mark beside it) both on the master and follower.  
![](./images/DisableAllSyncFeatures.png)  
  
