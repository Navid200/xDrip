## G7, One+ or Stelo troubleshooting  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7, One+ or Stelo troubleshooting  
<br/>  
  
If you have no readings, follow these steps in sequence.  
  
1- Disable [Engineering mode](../Engineering-Mode.md).  
  
2- Look at the sensor.  Do you see the filament coming out from the top?  
![](./images/G7FilamentNotInserted.png)  
If yes, take a picture so that you can show to Dexcom.  Even after you establish connectivity, it is highly unlikely it will ever work because the filament has not inserted under your skin.  You will need to use another sensor.  
  
3- Do you see "Mismatch - wait" on the Dex status page?  If yes, you may have entered the pairing code incorrectly, or you may have old used sensors nearby.  Move all your old used devices more than 10m away.  Make sure the pairing code is entered correctly.  It is printed on the applicator.  
Tap on "Restart Collector" on the classic status page.  
  
4- Do you have [proper connectivity](../Proper-connectivity.md)?  
If not, use a [scanner to confirm that you can see the transmitter](../Bluetooth-Scanner.md).  
You need to move all your old used devices more than 10m away.  Otherwise, you may see one of the old ones in the scanner and this could confuse xDrip as well showing "Mismatch - wait" on the status page.  
  
- If you have moved all the old devices away and can see the transmitter in the scanner, ensure [settings are correct](../G6-Recommended-Settings.md).  
  
- If you do not see the transmitter in the scanner, bring the applicator close to the sensor so that the magnet (shown in the image below) is near the sensor for a few seconds.  
![](./images/G7_Applicator_Magnet.png)  
This may trigger the sensor to activate.  Check if the transmitter is now visible in the scanner.  
  
Tap on "Restart Collector" on the classic status page.  
  
5- Do you see "Deep Sleeping Errors" or "Checking Auth Errors" or "Initializing Errors" on the Dex status page?  
- [Disable collection in xDrip](../Stop-xDrip.md).  
- Temporarily disable the pump or watch if you have any.  
- Wait 15 minutes for G7 to go back to rapid reconnect mode.  
- Delete any devices starting with DXCM, DX01 or DX02 from the list of connected or previously connected Bluetooth devices.  
- Re-enable xDrip Dex collector by changing the hardware data source to Dex.  
- Go to the status page and wait for a pair request in up to a minute and approve.  
  
<br/>  
  
---  
  
If you receive readings but experience random disconnections, please refer to this [link](../Intermittent.md) for further guidance on stabilizing the connection.  
  
