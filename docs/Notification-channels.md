## Notification Channels  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Alerts](./Alerts_page.md) >> Notification Channels  
  
You can enable notification channels at `Settings` &#8722;> `Alarms and Alerts` &#8722;> `Glucose Alerts Settings` &#8722;> `Notification Channels`.  
If you do, every alert will create a new Android notification channel (only on Android 8 and +) with specific settings.  You can edit the settings of each channel.  This will allow you to customize the notifications.  
![](./Alerts/images/AndroidNotificationSettings.png)  
  
Please note that if there is a discrepancy between a setting in xDrip and in the Android notification channel settings, Android settings will take effect.  For example, if you enable override silent mode in xDrip for an alert, but disable override do not disturb for the corresponding notification channel, your alert will not be heard when your phone is in DND.  
  
However, please note that there will be only one channel for all the alerts that are on the [Glucose Level Alerts List](Glucose-level-alerts.md).  
There is a known [bug](https://github.com/NightscoutFoundation/xDrip/issues/1503) that results in some channels missing editable settings on some Android 11 phones.  
  
