## Native Algorithm
[xDrip](../README.md) >> [Features](./Features_page.md) >> [Dexcom](./Dexcom_page.md) >> Native Algorithm  
  
When we use the native algorithm, the transmitter maintains the [calibration formula](./Calibration.md).  In non-native mode, xDrip maintains the formula using raw values that are also transmitted by the transmitter.  The non-native mode is only possible if the transmitter transmits raw values as well.  
A device that only transmits calibrated values, like G6 or G7, can only be used in native mode.  If you disable native algorithm when using such device, it will be automatically re-enabled.  
  
The two modes are compared in the following table.  
<br/> 
  
|        | Native | Non-Native | Notes |
|  :--------------:            | :----: |   :----:   |  :--:  |  
|To choose                   |Enable:  |Disable:     |Settings &#8722;> G5/G6/Dex1 Debug Settings <br/> &#8722;> Native Algorithm
|    Backfill                  | Yes    |  No        |Last 3 hours |  
|Session stops after <br/> 10 days \(7 for G5\) | Yes     | No        |In native mode, only [preemptive restart](./Preemptive-Restart.md) <br/> will keep the sensor going. |  
|G6 factory calibration        |Yes     |No          |  
|Transmitter stops after <br/> 110 days (112 for G5) * | Yes | No \*\* |Transmitter [hard reset](./Hard-Reset.md) will reset the <br/> number of days to 0. |  
| Relevant Calibration Graph | No | Yes | *** |  
|Clear calibrations| No | Yes | *** |  
  
\* The last day you can start a sensor in native mode is when transmitter days shows 99 \(104 for G5\).  
  
\*\*   If the G6 transmitter firmware version is 1.6.5.25 or prior to that, it transmits raw values as well.  Therefore, it can be used in non-native mode.  All G5 transmitters can be used in either native or non-native mode.  
Native mode is automatically enabled for a G6 except in [engineering mode](./Engineering-Mode.md).  
  
\*\*\*  In native mode, calibration is done by the transmitter.  If you see a calibration graph, it is because the transmitter sends raw values also.  But, the graph does not represent the calibration formula.  Clearing calibrations in native mode will have no impact on the effective calibration formula.
  
