## Restart G6 Sensor  
[xDrip](../README.md) >> [Features](./Features_page.md) >> [xDrip & Dexcom](./Dexcom_page.md) >> Restart G6 or Dexcom ONE sensor  
  
**<span style="color:red">This can be very dangerous.</span>**  Please read the entire page first.  You should not restart if you are not aware of the risks and consequences.  
**Your readings will be significantly high in error after warm-up after a restart.**  If you ignore this and take insulin to lower your readings, you will cause hypoglycemia.  Similarly, if you have a closed loop system and do not open the loop while readings are wrong, the loop will inject insulin, to correct the erroneous High, and cause hypoglycemia.    
**Please read the entire page and follow the required calibration steps.**  
  
If the transmitter firmware version, shown on the xDrip G5/G6/G7 status page, is 1.6.5.25 or prior to that, you can use preemptive restart.  Then, you will not need to restart.  xDrip preemptive restart will automatically restart the sensor and you will not need to restart manually.  Otherwise, you need to follow the instructions on this page to restart whether you use xDrip or another app or even the Dexcom receiver.  
  
Ensure [settings](./G6-Recommended-Settings.md) are correct first.   If your sensor has unexpectedly stopped, restarting will not resolve the underlying problem.  You should resolve any connectivity issues first and verify that you have [proper connectivity](./Proper-connectivity.md).  You don't need to restart before the sensor stops on day 10.  You can, but, you don't have to.  
  
You can use the original calibration code for restart or you can restart in no-code mode.  But, there are reports of malfunction in attempting to restart in no-code mode.  So, my recommended approach is to use the original calibration code as described below.   
  
1. Tap on stop sensor (top left menu). Tap on "STOP SENSOR".  
There will be "stop sensor" as a queue item on the G5/G6/G7 status page. The queue will clear in 5 minutes.  Then, you will see Sensor: Stopped on the G5/G6/G7 status page.  
Tap on stop sensor even if the sensor has already stopped if the "start sensor" option is not available.  Wait 5 minutes.  
Only after the "stop sensor" note has cleared the queue should you proceed to step 2.  
If day 10 has ended and the sensor has already stopped, and the "Start Sensor" option is already available under the top left menu, you can skip step 1.  
If you had a sensor start failed message, you may still see that message instead.  
2. Remove the transmitter without causing damage to the sensor ([see how](./Remove-transmitter.md)).  There is petroleum jelly on the transmitter and sensor contacts. Don't wipe it off.  
3. Wait at least 10 minutes from when you remove the transmitter.  
4. Snap the transmitter back into the sensor.  
5. Start Sensor.  Enter the original calibration code when asked.      
6. After the 2-hour warm-up, your readings will be significantly high in error (*).  You will need to calibrate.  You should calibrate in small steps to avoid a confused calibration error ([see how](./Calibrate-after-G6Restart.md)).  
  
<br/>  
  
---  

\*  When we restart a G6 sensor, we fool the transmitter to think that we are starting a new sensor.  If we use the original calibration code, the transmitter thinks the sensor is on day 1 when in reality, the sensor is on day 11 if we restarted after 10 days.  The impact of the immune system on the sensor is different on day 1 versus day 11.  This discrepancy results in incorrect high readings after warm-up.  
  
---  
  
The manufacturer does not approve the restart of sensors.  
  
As a sensor ages, the accuracy diminishes. You should occasionally check your blood glucose, with your glucose meter, to verify accuracy, and calibrate to reduce error when needed.  
Eventually, the noise level may increase as well. [Noise](./Noise.md) is the rapid oscillatory fluctuation of readings that is not a real representation of your blood glucose.  
  
