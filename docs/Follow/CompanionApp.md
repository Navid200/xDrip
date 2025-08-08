## Companion App Mode
[xDrip](../../) >> [Features](../Features_page.md) >> xDrip Companion App mode  
  
xDrip can receive readings from notifications issued by another app. For example, if you have CamAPS installed and operational, you can use xDrip on the same phone to display readings from CamAPS.  Similarly, if you have the Dexcom app installed and collecting data from a transmitter, you can use the Companion App feature to receive readings from the Dexcom app. This feature does **not** require internet access.  
<br/>  
  
---  
  
#### **Steps to Enable Companion App Mode**  
1- Ensure notifications from the other app are visible on your phone.  Pull down from the top of the screen to view all notifications.  
  
2- Enable Companion App mode on the [Hardware Data Source page](../HardwareDataSource.md).  
A note will appear:  
![](./images/PleaseAllowNotificationAccess.png)  
  
Tap OK.  
You will be taken to Android Notification Access page.  
Select xDrip and grant access.  If you're unable to grant access, follow [this guide](../RestrictedSettings.md) to give restricted access to xDrip.  Once complete, disable and re-enable Companion App Mode.  This time, you should be able to grant permission.  
  
3- Ensure xDrip uses the same [unit of measurement](../Display/Units_HiLow.md) (mmol/L or mg/dL) as your companion app.  
<br/>  
  
---  
  
#### **Backfill**  
When using Companion App mode, if you leave your phone for more than 5 minutes, you might see missing readings in xDrip upon your return, even if the companion app has no missed readings.  
  
Enable the following setting: If you return after more than 5 minutes and see no missed readings in xDrip, this is ideal.  
  
Please note that this feature may not work on all phones, Android versions, or companion apps. Test and use if it works for you.  
  
`Settings` &#8722;> `Less common settings` &#8722;> `Bluetooth Settings` &#8722;> `Companion Bluetooth`  
![](./images/CompanionBluetoothSetting.png)  
<br/>  
  
---  

#### **Multiple Companion apps**  
The Companion App feature is designed to work with only one companion app at a time. If you have two different apps that xDrip can work with as companion apps, xDrip cannot be configured to collect data from only one of them. Using such a setup is not recommended.  

If you need to use two different apps simultaneously and xDrip can collect data from both in Companion App mode, you should disable notifications in one of the apps.  
  
