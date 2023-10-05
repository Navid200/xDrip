## Hard Reset  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Hard Reset  
  
Please disable [engineering mode](./Engineering-Mode.md).  There is no need to enable engineering mode in order to perform a hard reset.  
  
You can hard reset a G5 using xDrip.   
You can also hard reset a G6, using xDrip, if the Firmware version is 1.6.5.25 or prior to that.  
At this point, there is no way to hard reset any other one of G6 transmitters.  

If you hard reset a transmitter, the number of transmitter days, as shown on the system status page, will be reset to 0. This is only required if you want to use the Native mode, or if you want to use the Dexcom app or receiver. If you are using the xDrip non-native mode, you don't need to hard reset.  

If the battery voltage is low, you will need to replace the battery. Hard resetting the transmitter will not change the battery voltage.  

If you want to hard reset, do it when you don't have an active session. For example, when you stop a sensor before starting the next one.  

Before you can hard reset a transmitter using xDrip, you need to have [proper connectivity](./Proper-connectivity.md) to it.  
You should also disable the Dexcom app as you cannot link to the same transmitter at the same time with both xDrip and the Dexcom app.  

Tap on the treatment (dropper) symbol on the main screen.  
![](./images/syringe-symbol.png)
  
This opens a form mainly showing a numeric pad. At the bottom right corner, long press on the microphone symbol.  
![](./images/treatment-menu.png)
  
A keyboard will appear. Type and enter `Hard Reset Transmitter`.  You will see a message confirming the attempt will be made.  Do not exit xDrip.  The command still needs to be sent to the transmitter.  
  
Monitor the system status page. It will take a read cycle ( up to 5 minutes) for the command to be submitted to the transmitter.  
You will see a warning that hard reset may have failed.  
Either restart the phone, or force stop xDrip and then, start it again.  
Delete the transmitter from the list of previously connected Bluetooth devices from your Android settings.  
  
If/when you intend to use the transmitter, after a reset, you will need to activate it again as if you were using a brand new transmitter for the very first time.  
  
