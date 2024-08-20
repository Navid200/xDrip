## Hard Reset (Advanced)  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Hard Reset  
  
Warning: This action cannot be undone. Ensure you really need to perform a hard reset before proceeding.  
  
Disable [engineering mode](./Engineering-Mode.md). Engineering mode is not required for a hard reset.   
  
G6 can be hard reset using xDrip if the firmware version is 1.6.5.25 or earlier. Transmitters with newer firmware cannot be hard reset without modifications.  
  
* Effect of Hard Reset:  
The transmitter's days count, as shown on the system status page, will reset to 0.  
  
* Timing for Hard Reset:  
 Perform the hard reset when you do not have an active session. For example, after stopping a sensor but before starting a new one.  
  
* Steps to Hard Reset a Transmitter Using xDrip:  
  
1- Ensure [proper connectivity](./Proper-connectivity.md) to the transmitter.  
  
2- Disable the Dexcom app as you cannot use both xDrip and the Dexcom app with the same transmitter simultaneously.  
  
3- On the xDrip main screen, tap the treatment (dropper) symbol.  
![](./images/syringe-symbol.png)  
  
4- This will open a form with a numeric pad. At the bottom right corner, long-press the microphone symbol.  
![](./images/treatment-menu.png)  
  
5- A keyboard will appear. Type `Hard Reset Transmitter` and tap Enter. A confirmation message will appear indicating that the attempt will be made. Do not exit xDrip; the command still needs to be sent to the transmitter.  
  
6- Monitor the system status page. The command may take up to 5 minutes to be submitted to the transmitter. You will see a warning that the hard reset may have failed.  Restart your phone or force stop xDrip and then restart it.  
  
7- Delete the transmitter from the list of previously connected Bluetooth devices in your Android settings.  
  
* Post-Reset:  
If you intend to use the transmitter after the reset, [activate it](./Dexcom/NewG6TX_Activation.md) as if it were a new transmitter.  
  
