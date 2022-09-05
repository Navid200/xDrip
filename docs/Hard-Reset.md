## Hard Reset  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> [Hard Reset](./Hard-Reset.md)  
  
Please disable [engineering mode](./Engineering-Mode.md).  No need to enable engineering mode in order to perform a hard reset.  
  
You can hard reset a G5.  
You can also hard reset a G6 if the serial number starts with 80.  
You can also hard reset a G6 with a serial number that starts with 81 if the Firmware version is 1.6.5.25 or prior to that.  
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


A keyboard will appear. Type "Hard Reset Transmitter" without the quotes.  You will see a message confirming the attempt will be made.  

Monitor the system status page. It will take several read cycles (5 minutes each) for the messages in the queue to clear.  
You may see a message warning that hard reset may have failed.  
Restart the phone.  
