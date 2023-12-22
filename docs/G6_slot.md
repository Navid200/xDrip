## G6 Bluetooth Slots
[xDrip](../README.md) >> [Features](Features_page.md) >> [Dexcom](./Dexcom_page.md) >> G6 Bluetooth Slots  

**<span style="color:red">WARNING</span>**  
This feature was added and placed behind engineering mode for verification before being offered as a feature.  
Unfortunately, tests have shown unexpected results.  Some transmitters have even been permanently disabled as a results.  
That's why it has not been removed from engineering mode.  
Therefore, for now, the use of this feature is not recommended.  
<br/>  
  
A G6 transmitter has 2 communication slots.  That's why you can use a t:slim pump and also see your readings in xDrip.  Or, you can use both the Dexcom receiver and xDrip at the same time.  
By default, xDrip is on slot 2.  t:slim pump is  on slot 1.  The Dexcom receiver is also on slot 1.  

If you want to change the slot number xDrip uses, you first need to enable [engineering mode](./Engineering-Mode.md).  
Then, if you access Settings &#8722;> G5/G6 Debug Settings, you will see a setting titled "Manual Slot Number", which you can edit.  
![](./images/manual_slot.png)  

To force xDrip to use the non-default slot number, set that to 1.  
If you disable engineering mode, it will use the default setting.  
If you want to go back to the default setting, in engineering mode, set it to 2.  
  
If you want to use two different collector apps, using the two slots, the two apps cannot be on the same phone.  
