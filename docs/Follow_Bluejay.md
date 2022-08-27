## BlueJay Follower  
August 25, 2022 

You can have xDrip show readings collected by your BlueJay.  Since BlueJay and xDrip communicate using Bluetooth, your phone and BlueJay need to be close for this to work.  It will not work over the internet.  
You need to link BlueJay to xDrip using the xDrip BlueJay Admin page.  
The following image shows the xDrip BlueJay settings page.  
![](./images/BluejaySettings.png)  


You have two options depending on which one of the two G6 transmitter slots can be used by BlueJay.  The following presents both options.  
<br/>  
  
### 1- BlueJay can use the medical (non-phone) slot  
Bluejay, by default, uses the medical slot of a G6.  If you have another device or app occupying the phone slot, for example CamAPS, and cannot use xDrip to collect from your G6, you can set up xDrip to receive readings from your Bluejay.  
  
You first need to set up your Bluejay as an independent collector.  
Then, you should select "640G / EverSense" as the xDrip hardware data source.  
![](images/HDS_Eversense.png)  
  
The following image shows the xDrip BlueJay Advanced Settings page.    
![](./images/BluejayAdvancedSettings.png)  
<br/>  
  
### 2- BlueJay can use the phone slot  
If you have a device occupyiong the medical slot, for example, the Dexcom receiver, you will not be able to have both xDrip and BlueJay collect from your G6 transmitter.  In this case, BlueJay is forced to use the phone slot.  You need to start from default settings where xDrip is set to collect from the phone slot with the correct transmitter ID entered in xDrip.  Therefore, "G5/G6 Transmitter" is selected as the hardware data source in xDrip.  And, you should have BlueJay collect from the G6 transmitter.  Then, on the xDrip Advanced BlueJay Settings page, you need to disable phone collector and enable "BlueJay uses Phone slot" as shown in the following image.  
![](./images/BluejayAdvancedSettings2.png)  
  
