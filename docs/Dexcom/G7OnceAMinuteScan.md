## G7 Once a minute scan mode  
[xDrip](../../README.md) >> [Features](../Features_page.md) >> [xDrip & Dexcom](../Dexcom_page.md) >> Dexcom G7 once a minute scan  
  
After it was reported that G7 readings appeared at intervals other than once every 5 minutes upon a disconnect, I performed a test.  
  
I installed a [Bluetooth scanner](https://navid200.github.io/xDrip/docs/Bluetooth-Scanner.html) on a test phone while using a G7 paired to my main phone.  
Once every 5 minutes, the scanner showed the transmitter to transmit for about 15 seconds.  This is exactly how a G6 behaves.  

I then placed my main phone inside a microwave.  This stopped it from maintaining a Bluetooth handshake with the G7 device.  
The scanner showed that G7 continued to transmit once every 5 minutes for 2 or 3 more cycles.  
Then, G7 went into a more frequent scan mode where it transmitted once every minute for the same period of 15 seconds each.  
  
I then removed my main phone from the microwave.  It established connectivity to G7 and backfilled all the readings.  G7 went back to transmitting once every 5 minutes again in sync with the original cycle.  
  
This tells me that G7 behaves differently when the paired device fails to maintain a link and goes into a more rapid transmission mode.  Upon recovery, it returns to the original once a minute cycle.  

The total number of readings remain equal to once every 5 minutes.  If you have frequent disconnects, some of your readings are not going to be on the once every 5 minute time grid.  They will move forward in time by 1 to 4 minutes.  But, the time interval after will be longer.  The total number of readings remain the same as before, equal to one every 5 minutes.  

  
