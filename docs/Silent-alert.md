## xDrip Alert makes no sound
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Alerts](./Alerts_page.md) >> xDrip Alert makes no sound  
  
Different factors could result in an xDrip alert not making any sound.  The following is a list of such combinations.  
  
1- The alert is on the [Glucose Level Alerts List](./Glucose-level-alerts.md) and you have chosen the silent [volume profile](./Volume-profiles.md).  
  
2- The alert is not on the [Glucose Level Alerts List](./Glucose-level-alerts.md) and the phone `ring & notification` volume is set to 0.  
  
3- The [notification channels](./Notification-channels.md) setting is enabled and the Android notification channel corresponding to the alert has been set to be silent.  
  
4- Android is not set to allow xDrip notifications to make sound.  To remedy this, enable the following two settings.  
`Android Settings` &#8722;> `Apps & notifications` &#8722;> `Notifications` &#8722;> `xDrip+` &#8722;> `All xDrip+ notifications`  
`Android Settings` &#8722;> `Apps & notifications` &#8722;> `Notifications` &#8722;> `xDrip+` &#8722;> `Uncategorized` &#8722;> `Allow interruptions`  
  
5- The phone is in `Do Not Disturb` mode, and Android is not set to exclude Alarms from do not disturb.  To remedy this, enable Android `Settings` &#8722;> `Sound` &#8722;> `Do Not Disturb` &#8722;> `Alarm & other interruptions` &#8722;> `Alarms`  
![](./Alerts/images/ExcludeAlarms.png)  
  
6- The phone is in `Do Not Disturb` mode and the `override silent mode` and `Force speaker` settings are not enabled for the alert.  The missed reading alert inherits its sound file and override silent mode from the `Other Alerts` (`Settings` &#8722;> `Alarms and Alerts` &#8722;> `Other Alerts`).  
![](./Alerts/images/OverrideAndForce.png)  

7- The phone is in `Do Not Disturb` mode, and Android is not set to allow xDrip to overside DND.  To remedy this, change the setting at `Android Settings` &#8722;> `Apps` &#8722;> `xDrip+` &#8722;> `Notifications` &#8722;> &#8722;> `Override Do Not Disturb`  
![](./Alerts/images/AndrdxDripDND.png)  
  
<br/>  
  
---  
  
When an alert triggers, a log is created including the alert title and the time it triggered.  If the alert triggers and you can see the log but cannot hear it, and the above list does not explain the behavior, take a screenhot of the log to include with your request for help.  
  
