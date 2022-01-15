## What not to do!  
  
**Don't change the transmitter ID to a new one until you intend to use it.**  
As soon as you establish connectivity to a new transmitter, even if you don't start a sensor, the counter (on the transmitter) starts counting the number of days.  Since the number of days you can use a transmitter is [limited](./Transmitter-lifetime.md), don't connect unless you intend to start using the transmitter.  

**Don't start a sensor on a new G6 transmitter before voltages are shown.**  
Only start a sensor after you have established [connectivity](/Proper-connectivity.md), and can see the voltages reported on the system status page as explained.  
When you first establish connectivity to a transmitter, it will take a few read cycles for everything to be properly synced.  You can start a sensor right away.  You shouldn't!  See the [details](./Starting-G6.md).  

**Don't start a G6 sensor unless ...**  
Before you start a sensor, the transmitter must have been outside a sensor for at least 10 minutes.  Otherwise, the transmitter will conclude that you are attempting to restart and will fail.  
A mistake some make with G6 is to place a new sensor on with the previous sensor still running.  Then, they stop the old sensor, remove the transmitter and snap it into the new sensor, right away, and start.  Even though the sensor is a new sensor, since the transmitter has not been kept out of both sensors for at least 10 minutes, the transmitter will stop the new sensor and issue an error.  

**When you get a new phone, ...**   
Don't forget to [disable collection](./Stop-xDrip.md) on the old phone.  Otherwise, both phones will have constant connectivity issues.  

**Don't restart** a G6 sensor unless you accept responsibility for the possible consequences.  The manufacturer does not approve of restarting sensors.  You need to read all the [details and consequences of a restart](./Restart-G6-Sensor.md) and accept responsibility.  I don't encourage anyone to restart sensors, or anything else the manufacturer does not approve.  

**Don't use a Nightly build** unless you are aware of the consequences.  
A Nightly build is released to offer a new development to the users.  However, a Nightly build could result in unexpected behavior as it has not been tested in detail.  There is no way the developers can test all devices that are available in the market.  So, the consequence of using a Nightly is taking the chance of experiencing an unexpected behavior.  

**Don't open an issue when you experience a problem**  
Please don't open an issue, on Github, by default as the first step when you run into a problem.  
Please post in [Discussions](https://github.com/NightscoutFoundation/xDrip/discussions) or [facebook](https://www.facebook.com/groups/xDripG5) instead.  
