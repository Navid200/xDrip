## Native Algorithm  
  
The sensor has no direct access to blood.  It has access to interstitial fluid.  The interstitial fluid glucose and blood glucose are correlated.  The sensor measures glucose in interstitial fluid.  From that, the CGM calculates the blood glucose.  A formula is used to do the calculation.  When we perform a blood glucose measurement, with our glucose meter, and enter the reading into the continuous glucose monitoring system (CGM), and perform a calibration, the CGM updates the formula using the current interstitial fluid glucose and the blood glucose reading we just entered.  

When we use the native algorithm, the transmitter maintains the calibration formula.    In non-native mode, xDrip maintains the formula.  The following table provides a comparison.   
<br/><br/>
 

| Behavior | Native | Non-Native | Notes |
|  :--------------:            | :----: |   :----:   |  :--:  |  
|Activate                      |Select:  |Deselect:     |Settings &#8722;> G5/G6 Debug Settings <br/> &#8722;> Native Algorithm
|    Backfill                  | Yes    |  No        |Last 3 hours |  
|Sensors keep going <br/> beyond 7/10 days for G5/G6 |No      | Yes        |In native mode, only [preemptive restart](./Preemptive-Restart.md) <br/> will keep the sensor going. |  
|G6 factory calibration        |Yes     |No          |  
|Transmitter stops after <br/> 110 days (112 for G5) | Yes | No \*\* |Transmitter [hard reset](./Hard-Reset.md) will reset the <br/> number of days to 0. |  
| Relevant Calibration Graph | No | Yes | * |  
|Clear calibrations| No | Yes | * |  


\* In native mode, calibration is done by the transmitter.  If you see a calibration graph, it is because the transmitter sends raw values also.  But, the graph does not represent the calibration formula.  Clearing calibrations in native mode will have no impact on the effective calibration formula.  
  
\*\*   If the second character of a G6 transmitter serial number is not 0 or 1, it does not transmit raw values.  Therefore, it cannot be used in non-native mode.  All G5 transmitters can be used in either native or non-native mode.  
A G6 can only be used in non-native mode (only 80XXXX or 81XXXX) in [engineering mode](./Engineering-Mode.md).  
